# Super Downloader
> Download somebody's files in sneaky way!!

## How to Make

You will have two `Makefiles` in `client` and `server` directory so to make it individually
>make

## About My Project 
The project contains several components:
1. `Fuzzy Matching` technique from [fuzzy-match](https://github.com/philj56/fuzzy-match)
2. Downloading selected files from the server side.
    (This will iterate all your directories and search for every `regular files`)

## How to Execute
1. executables in server side : `server` `<port>`
>./server 10000
2. executables in client side : `client` `<Server IP>` `<port>` 
>./client 127.0.0.1 10000
3. Type any text to search
4. Use arrow key `up` `down` to select from searched files
5. Press enter to download the file you want
6. Step 3-5 will be repeated
7. Press `*` to exit
