#include <stddef.h>
#include <stdio.h>
#define BUF_SIZE 1024

typedef struct file{
    char dir[1024];
    off_t size;
    int idx;
    int score;
} file;
