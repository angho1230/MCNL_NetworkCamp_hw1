#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/types.h>
#include <dirent.h>

#include "file.h"

static int fsize = 128;
static int fcount = 0;
static file* files = 0x0;

file* get_files(){
    if(files){
        printf("Passing existing files\n");
        return files;
    }
    files = (file*)malloc(fsize*sizeof(file));
    memset(files, 0, fsize*sizeof(file));
    int r = file_iter(".");
    if(r != 0){
        return 0x0;
    }
    return files;
}

int file_iter(char * dir){
    DIR * d;
    struct dirent* dirst;
    struct stat stst;
    int result = 0;
    if ((d = opendir(dir)) == 0x0){
        printf("Failed opening %s\n", dir);
        return 1;
    }
    if(stat(dir, &stst) == -1){
        printf("Failed to stat %s\n", dir);
    }
    while((dirst = readdir(d)) != 0x0){
        if((dirst->d_type == DT_DIR &&  dirst->d_name[0] != '.') || 
           dirst->d_type == DT_REG){
            char * newdir = (char *)malloc(BUF_SIZE);
            sprintf(newdir, "%s/%s", dir, dirst->d_name);
            if(dirst->d_type == DT_REG){
                struct stat fst; 
                if(stat(newdir, &fst) == -1){
                    printf("Failed to stat %s\n", dir);
                    return 1;
                }
                files[fcount] = make_file(newdir, fst.st_size);
                fcount++;
                if(fcount >= fsize-1){
                    fsize *= 2;
                    file* newfiles = (file*)malloc(fsize*sizeof(file));
                    memset(newfiles, 0, fsize*sizeof(file));
                    memcpy(newfiles, files, fsize/2*sizeof(file));
                    free(files);
                    files = newfiles;
                }
            }
            if(dirst->d_type == DT_DIR){
                if(file_iter(newdir) != 0){
                    result = 1;
                }
                free(newdir);
            }
        }
    }
    return result;
}

file make_file(char * dir, off_t size){
    file f; 
    f.dir = dir;
    f.size = size;
    return f;
}

void print_file(file f){
    printf("%s(%ld)", f.dir, f.size);
}

/*
int traverseCopyDir(const char *dir, const char *dst, int v){
    DIR* d;
    int result = 0;
    struct dirent* dirst;
    struct stat stst;
    if((d = opendir(dir)) == 0x0){
        printf("Failed opening %s\n", dir);
        return 1;
    }
    if(stat(dir, &stst) == -1){
        printf("Failed to stat %s\n", dir);
    }
    if(mkdir(dst, stst.st_mode) == 0){
        if(v) printf("Copy Directory: %s -> %s\n", dir, dst);
        // Does not print when there already exists directory
    }
    while((dirst = readdir(d)) != 0x0){
        if((dirst->d_type == DT_DIR && strcmp(dirst->d_name, ".") != 0 && strcmp(dirst->d_name, "..") != 0) ||
            dirst->d_type == DT_REG){
            char subsrc[1024];
            char subdst[1024];
            sprintf(subdst, "%s/%s", dst, dirst->d_name);
            sprintf(subsrc, "%s/%s", dir, dirst->d_name);
            if(dirst->d_type == DT_REG){
                if (copyFileFile(subsrc, subdst, v) != 0) {
                    result = 1;
                }
                continue;
            }
            if(traverseCopyDir(subsrc, subdst, v) != 0){
                result = 1;
            }
        }
    }
    return result;
}
*/
