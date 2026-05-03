#include <stdlib.h>
#include <stdio.h>
int _x1;
int _x2;
int _x3;

int main() {
_x1 = 10;
_x2 = 5;
_x3 = 0;
_x3 = (_x1 + _x2);
_x3 = (_x1 + _x2);
printf("Sum: %d\n", _x3);
if ((_x3 < 11)) goto TRUE_LABEL1;
goto FALSE_LABEL2;
TRUE_LABEL1:
printf("not greater\n");
goto END_LABEL3;
FALSE_LABEL2:
printf("greater\n");
END_LABEL3:
if ((_x2 < _x1)) goto TRUE_LABEL4;
goto FALSE_LABEL5;
TRUE_LABEL4:
printf("x wins\n");
goto END_LABEL6;
FALSE_LABEL5:
END_LABEL6:
}

