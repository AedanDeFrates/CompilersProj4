#include <stdlib.h>
#include <stdio.h>
char* _x1;
char* _x2;
char* _x3;
char* _x4;

int main() {
_x1 = "test.txt";
_x2 = "Hello, file system!\n";
{ FILE* _f = fopen(_x1, "w"); if(_f){ fputs(_x2, _f); fclose(_f); } }
_x4 = malloc(4096);
{ FILE* _f = fopen(_x1, "r"); if(_f){ fgets(_x4, 4096, _f); fclose(_f); } }
_x3 = _x4;
_x3 = _x4;
printf("%s", _x3);
}

