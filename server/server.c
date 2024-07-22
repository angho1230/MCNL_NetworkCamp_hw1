#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include "file.h"
#include "fuzzy_match.h"

#define PSIZE 1024
void error_handling(char * message);

int main(int argc, char* argv[]){
    int serv_sd, clnt_sd;
    FILE * fp;
    char pattern[PSIZE];
    char buf[BUF_SIZE];
    char message[1024];
    int read_cnt;
    int str_len;

    file* files = get_files();
    /*
*/
    struct sockaddr_in serv_adr, clnt_adr;
    socklen_t clnt_adr_sz;

    if(argc != 2){
        printf("Usage %s <port>\n", argv[0]);
        exit(1);
    }

    serv_sd=socket(PF_INET, SOCK_STREAM, 0);
    if(serv_sd == -1){
        error_handling("socket() error");
    }

    memset(&serv_adr, 0, sizeof(serv_adr));
    serv_adr.sin_family = AF_INET;
    serv_adr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_adr.sin_port = htons(atoi(argv[1]));

    if(bind(serv_sd, (struct sockaddr*)&serv_adr, sizeof(serv_adr)) == -1){
        error_handling("bind() error");
    }

    if(listen(serv_sd, 5) == -1){
        error_handling("listen() error");
    }

    clnt_adr_sz=sizeof(clnt_adr);
    clnt_sd = accept(serv_sd, (struct sockaddr *)&clnt_adr, &clnt_adr_sz);
    if(clnt_sd == -1){
        error_handling("accept() error");
    }
    else{
        printf("Connected client\n");
    }

    int c;
    while(1){
        c = read(clnt_sd, &c, sizeof(int));
        
        if(c == '*'){
            break;
        }
        write(clnt_sd, message, str_len);
        int idx = 0;
        while(files[idx].dir != 0x0){
            
            sprintf(message, "%d:%s(%ld)\n", idx+1, files[idx].dir, files[idx].size);
            idx++;
            write(clnt_sd, message, strlen(message));
        }
    }
    printf("Closing.....");
    close(serv_sd);
    close(clnt_sd);
}

void error_handling(char* message){
    fputs(message, stderr);
    fputc('\n', stderr);
    exit(1);
}
