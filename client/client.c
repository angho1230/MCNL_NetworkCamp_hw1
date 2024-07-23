#include <netinet/in.h>
#include <ctype.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <errno.h>
#include <sys/socket.h>
#include <termios.h>
#include "file.h"
#include "fuzzy_match.h"

#define ESC '\033'
#define AUP 'A'
#define ADOWN 'B'
#define SSIZE 30 
int sortbyscore(const char* pattern, file* files, int fcount, file* fsort, int prefix);
int prefix_change(file* files, int fcount, file* fsort, int prefix);
void error_handling(char* message);
int get_valid(file* files, int fcount);

void color_output(const char* pattern, const file * fsort, int fcount, int idx);

int mkdirs(const char *path, mode_t mode)
{
    char tmp_path[2048];
    const char *tmp = path;
    int  len  = 0;
    int  ret;

    if(path == NULL || strlen(path) >= 2048) {
        return -1;
    }

    while((tmp = strchr(tmp, '/')) != NULL) {
        len = tmp - path;
        tmp++;

        if(len == 0) {
            continue;
        }
        strncpy(tmp_path, path, len);
        tmp_path[len] = 0x00;

        if((ret = mkdir(tmp_path, mode)) == -1) {
            if(errno != EEXIST) {
                return -1;
            }
        }
    }
    return mkdir(path, mode);
}

int main(int argc, char * argv[]){
    int c;
    static struct termios t, oldt;
    tcgetattr( STDIN_FILENO, &t);
    oldt = t;
    t.c_lflag &= ~(ICANON | ECHO);
    tcsetattr( STDIN_FILENO, TCSANOW, &t);
    while(1){
        void* files_v;
        int fcount;
        char pattern[1024];
        int sock;
        char buf[BUF_SIZE];
        int str_len;
        struct sockaddr_in serv_adr;

        if(argc != 3){
            printf("Usage : %s <IP> <port>\n", argv[0]);
            exit(1);
        }

        sock=socket(PF_INET, SOCK_STREAM, 0);
        if(sock == -1){
            error_handling("socket() error");
        }

        memset(&serv_adr, 0, sizeof(serv_adr));
        serv_adr.sin_family = AF_INET;
        serv_adr.sin_addr.s_addr = inet_addr(argv[1]);
        serv_adr.sin_port = htons(atoi(argv[2]));
        if(connect(sock, (struct sockaddr*)&serv_adr, sizeof(serv_adr)) == -1){
            error_handling("connect() error!");
        }

        printf("Connected.......\r");
        read(sock, &fcount, sizeof(int));
        int total_size = fcount * sizeof(file);
        files_v = (void*)malloc(total_size);
        int read_size = 0;
        while(total_size > read_size){
            int len = read(sock, files_v+read_size, sizeof(file));
            if(len <= 0){
                printf("error in reading files\n");
                exit(1);
            }
            read_size += len;
        }

        file* files = (file*)files_v;
        printf("Read files.......\r");
        fflush(stdout);
        printf("\e[1;1H\e[2J");
        printf("Type the name of the file :\n");
        file filetop[SSIZE];

        int ss = SSIZE;
        for(int i = 0; i < SSIZE; i++){
            if(i >= fcount - 1){
                ss = i+1;
            }
            filetop[i] = files[i];
        }
        int idx = 0;
        int len = 0;
        int prefix = 0;
        int valid = fcount;
        int end = 0;
        while((c = getchar()) != '\n'){
            printf("\e[1;1H\e[2J");
            switch(c){
                case ESC:
                    getchar();
                    c = getchar();
                    if(c == AUP){
                        if(idx > 0)
                            idx--;
                        else if(prefix > 0){
                            prefix--;
                            ss = prefix_change(files, fcount, filetop, prefix);
                        }
                    }
                    else if(c == ADOWN){
                        if(idx < ss-1)
                            idx++;
                        else if(prefix < valid - ss){
                            prefix++;
                            ss = prefix_change(files, fcount, filetop, prefix);
                        }
                    }
                    printf("Type the name of the file : %s\n", pattern);
                    color_output(pattern, filetop, ss, idx);
                    break;
                case 127:
                case 8:
                    if(len > 0)len--;
                    pattern[len] = '\0';
                    printf("Type the name of the file : %s\n", pattern);
                    ss = sortbyscore(pattern, files, fcount, filetop, prefix);
                    valid = get_valid(files, fcount);
                    color_output(pattern, filetop, ss, idx);
                    break;
                case '*':
                    end = 1;
                    break;
                default:
                    pattern[len] = c;
                    len++;
                    pattern[len] = '\0';
                    printf("Type the name of the file : %s\n", pattern);
                    ss = sortbyscore(pattern, files, fcount, filetop, prefix);
                    valid = get_valid(files, fcount);
                    color_output(pattern, filetop, ss, idx);
                    break;
            }
            if(end) break;
        }
        if(end) break;
        write(sock, &filetop[idx].idx, sizeof(int));
        printf("Downloading %s\n", filetop[idx].dir);
        fflush(stdout);
        int read_len;
        int dsize = filetop[idx].size;
        read_size = 0;

        char * cdir = strndup(filetop[idx].dir, strlen(filetop[idx].dir));
        char * ci = cdir;
        char * ciprev = ci;
        while((ci = strchr(ci+1, '/')) != 0x0){
            printf("%s\n", ci);
            ciprev = ci;
        }
        *ciprev = '\0';
        mkdirs(cdir, 0700);
        printf("making directory %s\n", cdir);
        fflush(stdout);
        free(cdir);

        FILE* fp = fopen(filetop[idx].dir, "wb");
        while((read_len = read(sock, buf, BUF_SIZE)) != 0){
            fwrite(buf, 1, read_len, fp) ;
        }
        printf("Download complete\n");
        close(sock);
        sleep(1);
    }
    printf("Closing....\n");
    fflush(stdout);
    tcsetattr( STDIN_FILENO, TCSANOW, &oldt);
    return 0;
}

void error_handling(char* message){
    fputs(message, stderr);
    fputc('\n', stderr);
    exit(1);
}

void swap(file* a, file* b) 
{ 
    file temp = *a; 
    *a = *b; 
    *b = temp;
} 

int partition(file arr[], int low, int high) 
{ 

    int pivot = arr[low].score; 
    int i = low; 
    int j = high;

    while (i < j) {

        while (arr[i].score > pivot && i <= high - 1) { 
            i++; 
        } 

        while (arr[j].score <= pivot && j >= low + 1) {
            j--; 
        }
        if (i < j) {
            swap(&arr[i], &arr[j]);
        } 
    } 
    swap(&arr[low], &arr[j]); 
    return j; 
} 

void quickSort(file arr[], int low, int high) 
{ 
    if (low < high) { 

        int partitionIndex = partition(arr, low, high); 

        quickSort(arr, low, partitionIndex - 1); 
        quickSort(arr, partitionIndex + 1, high); 
    } 
} 

int sortbyscore(const char* pattern, file* files, int fcount, file* fsort, int prefix){
    for(int i = 0; i < fcount; i++){
        files[i].score = fuzzy_match(pattern, files[i].dir);
    }
    quickSort(files, 0, fcount-1);
    int idx = 0;
    for(int i = prefix; i < fcount; i++){
        if(i > SSIZE-1+prefix) return SSIZE;
        if(files[i].score == INT32_MIN){
            return i;
        }
        fsort[idx++] = files[i];
    }
    return fcount;
}

int prefix_change(file* files, int fcount, file* fsort, int prefix){
    int idx = 0;
    for(int i = prefix; i < fcount; i++){
        if(i > SSIZE-1+prefix) return SSIZE;
        if(files[i].score == INT32_MIN){
            return i;
        }
        fsort[idx++] = files[i];
    }
    return fcount;
}

int get_valid(file* files, int fcount){
    int n = 0;
    for(int i = 0; i < fcount; i++){
        if(files[i].score != INT32_MIN){
            n++;
        }
        else break;
    }
    return n;
}
void red () {
    printf("\033[1;31m");
}

void reset () {
    printf("\033[0m");
}

void color_output(const char* pattern, const file * fsort, int fcount, int idx){
    int plen = strlen(pattern);
    for(int i = 0; i < fcount; i++){
        int pi = 0;
        int len = strlen(fsort[i].dir);
        if(idx == i){
            printf(">%d | ", fsort[i].idx);
        }
        else{
            printf(" %d | ", fsort[i].idx);
        }
        for(int j = 0; j < len; j++){
            if(tolower(fsort[i].dir[j]) == tolower(pattern[pi])){
                red();
                printf("%c", fsort[i].dir[j]);
                reset();
                pi++;
            }else{
                printf("%c", fsort[i].dir[j]);
            }
        }
        printf(" (%lu)\n", fsort[i].size);
    }
}

