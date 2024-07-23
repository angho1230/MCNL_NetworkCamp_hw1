#include <netinet/in.h>
#include <ctype.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
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
int main(int argc, char * argv[]){
    int c;
    static struct termios t;
    tcgetattr( STDIN_FILENO, &t);
    t.c_lflag &= ~(ICANON | ECHO);
    tcsetattr( STDIN_FILENO, TCSANOW, &t);
    
    void* files_v;
    int fcount;
    char pattern[1024];
    int sock;
    char message[BUF_SIZE];
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
    }

    write(sock, &filetop[idx].idx, sizeof(int));
    while(1){
        str_len = read(sock, message, BUF_SIZE-1);
        if(message[str_len-1] == EOF){
            message[str_len-1] = '\0';
            printf("%s", message);
            break;
        }
        message[str_len] = '\0';
        printf("%s", message);
    }

    printf("Closing....\n");
    close(sock);
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

