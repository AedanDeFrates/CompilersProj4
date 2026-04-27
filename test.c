#include <stdlib.h>
#include <stdio.h>
int* _x1;
char* _x2;
int* _x3;
int* _x4;
int* _x5;
int* _x6;
int* _x7;
int _x8;

int main() {
_x7 = "1";
_x6 = 2;
return _x6;
if ((_x8 < 5)) goto TRUE_LABEL1;
goto FALSE_LABEL2;
TRUE_LABEL1:
return _x8;
goto END_LABEL3;
FALSE_LABEL2:
return 1;
END_LABEL3:

START_LABEL4:
if ((_x8 < 0)) goto BODY_LABEL5;
goto END_LABEL6;
BODY_LABEL5:
_x8 = (_x8 + 1);
_x8 = (_x8 + 1);
goto START_LABEL4;
END_LABEL6:
}

