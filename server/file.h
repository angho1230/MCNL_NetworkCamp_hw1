#include <stddef.h>
#include <stdio.h>
#define BUF_SIZE 1024

typedef struct file{
    char * dir;
    off_t size;
} file;

file* get_files();
int file_iter(char * dir);
file make_file(char * dir, off_t size);
void print_file(file f);
