#include <stdlib.h>
#include <stdio.h>
int _x1;

int num() {
return 42;
}

int main() {
_x1 = (num() + 4);
printf("%d\n", _x1);
return 0;
}

