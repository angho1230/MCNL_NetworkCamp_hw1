#include <netinet/in.h>
#include <stdint.h>
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
    int read_cnt;
    int str_len;

    file* files = get_files();
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
    while(1){
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
        int fcount = get_file_count();
        write(clnt_sd, &fcount, sizeof(int));
        for(int i = 0; i < fcount; i++){
            write(clnt_sd, &files[i], sizeof(file));
        }
        printf("Sending files done\n");
        int idx;
        int len = 0;
        read(clnt_sd, &idx, sizeof(int));
        printf("selected %d\n", idx);
        printf("sending %s\n", files[idx].dir);
        fp = fopen(files[idx].dir, "rb");

        while(1){
            read_cnt = fread(buf, 1, BUF_SIZE, fp);
            if(read_cnt < BUF_SIZE){
                write(clnt_sd, buf, read_cnt);
                break;
            }
            write(clnt_sd, buf, read_cnt);
        }
        printf("sent %d bytes\n", len);
        shutdown(clnt_sd, SHUT_WR);
    }
    close(serv_sd);
    close(clnt_sd);
}

void error_handling(char* message){
    fputs(message, stderr);
    fputc('\n', stderr);
    exit(1);
}
