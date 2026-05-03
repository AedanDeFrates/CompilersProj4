#include <stdlib.h>
#include <stdio.h>
int* _x1;
int _x2;
int _x3;
int _x4;
int _x5;
int _x6;
int _x7;
int _x8;
int _x9;
int _x10;
int _x11;
int _x12;
int _x13;
int _x14;
int _x15;
int _x16;

void swap() {
_x9 = *(_x1+_x3);
_x9 = *(_x1+_x3);
_x10 = _x3;
if ((_x10 >= _x2)) goto RESIZE_LABEL1;
goto NORESIZE_LABEL2;
RESIZE_LABEL1:
_x1 = realloc(_x1, sizeof(int*) * (_x10 + 1));

_x2 = (_x10 + 1);
NORESIZE_LABEL2:
*(_x1 + _x10) = *(_x1+_x8);

_x11 = _x8;
if ((_x11 >= _x2)) goto RESIZE_LABEL3;
goto NORESIZE_LABEL4;
RESIZE_LABEL3:
_x1 = realloc(_x1, sizeof(int*) * (_x11 + 1));

_x2 = (_x11 + 1);
NORESIZE_LABEL4:
*(_x1 + _x11) = _x9;

}

void bubbleSort() {
_x9 = 1;
START_LABEL5:
if ((!((_x9 < 1)))) goto BODY_LABEL6;
goto END_LABEL7;
BODY_LABEL6:
_x9 = 0;
_x9 = 0;
_x8 = 0;
_x8 = 0;
START_LABEL8:
if (((_x8 + 1) < _x6)) goto BODY_LABEL9;
goto END_LABEL10;
BODY_LABEL9:
if ((*(_x1+(_x8 + 1)) < *(_x1+_x8))) goto TRUE_LABEL11;
goto FALSE_LABEL12;
TRUE_LABEL11:
_x3 = _x8;
_x4 = (_x8 + 1);
swap();
_x9 = 1;
_x9 = 1;
goto END_LABEL13;
FALSE_LABEL12:
END_LABEL13:
_x8 = (_x8 + 1);
_x8 = (_x8 + 1);
goto START_LABEL8;
END_LABEL10:
goto START_LABEL5;
END_LABEL7:
}

int main() {
_x9 = 0;
_x12 = _x9;
if ((_x12 >= _x2)) goto RESIZE_LABEL14;
goto NORESIZE_LABEL15;
RESIZE_LABEL14:
_x1 = realloc(_x1, sizeof(int*) * (_x12 + 1));

_x2 = (_x12 + 1);
NORESIZE_LABEL15:
*(_x1 + _x12) = 5;

_x13 = _x9;
if ((_x13 >= _x2)) goto RESIZE_LABEL16;
goto NORESIZE_LABEL17;
RESIZE_LABEL16:
_x1 = realloc(_x1, sizeof(int*) * (_x13 + 1));

_x2 = (_x13 + 1);
NORESIZE_LABEL17:
*(_x1 + _x13) = 1;

_x14 = _x9;
if ((_x14 >= _x2)) goto RESIZE_LABEL18;
goto NORESIZE_LABEL19;
RESIZE_LABEL18:
_x1 = realloc(_x1, sizeof(int*) * (_x14 + 1));

_x2 = (_x14 + 1);
NORESIZE_LABEL19:
*(_x1 + _x14) = 4;

_x15 = _x9;
if ((_x15 >= _x2)) goto RESIZE_LABEL20;
goto NORESIZE_LABEL21;
RESIZE_LABEL20:
_x1 = realloc(_x1, sizeof(int*) * (_x15 + 1));

_x2 = (_x15 + 1);
NORESIZE_LABEL21:
*(_x1 + _x15) = 2;

_x16 = _x9;
if ((_x16 >= _x2)) goto RESIZE_LABEL22;
goto NORESIZE_LABEL23;
RESIZE_LABEL22:
_x1 = realloc(_x1, sizeof(int*) * (_x16 + 1));

_x2 = (_x16 + 1);
NORESIZE_LABEL23:
*(_x1 + _x16) = 3;

_x9 = 0;
_x9 = 0;
printf("Before: ");
START_LABEL24:
if ((_x9 < 5)) goto BODY_LABEL25;
goto END_LABEL26;
BODY_LABEL25:
printf("%d ", *(_x1+_x9));
_x9 = (_x9 + 1);
_x9 = (_x9 + 1);
goto START_LABEL24;
END_LABEL26:
printf("\n");
_x6 = 5;
bubbleSort();
_x9 = 0;
_x9 = 0;
printf("After: ");
START_LABEL27:
if ((_x9 < 5)) goto BODY_LABEL28;
goto END_LABEL29;
BODY_LABEL28:
printf("%d ", *(_x1+_x9));
_x9 = (_x9 + 1);
_x9 = (_x9 + 1);
goto START_LABEL27;
END_LABEL29:
printf("\n");
}

