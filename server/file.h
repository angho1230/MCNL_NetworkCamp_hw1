#include <stddef.h>
#include <stdio.h>
#define BUF_SIZE 1024

typedef struct file{
    char dir[1024];
    off_t size;
    int idx;
    int score;
} file;

file* get_files();
int file_iter(char * dir);
file make_file(char * dir, off_t size);
void print_file(file f);
int get_file_count();
