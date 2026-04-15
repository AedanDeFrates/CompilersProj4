// Minimum Grade: 80
fun int main() {
    var int a = 5;
    var int b = 3;
    var int c = 4;
    var int d = 1;
    var int e = 2;

    var int temp;
    var int swapped = 1;

    // Print before
    printf("Before: %d %d %d %d %d\n", a, b, c, d, e);

    while (!(swapped < 1)) {
        swapped = 0;

        if (a < b) {
            temp = a; a = b; b = temp;
            swapped = 1;
        }
        if (b < c) {
            temp = b; b = c; c = temp;
            swapped = 1;
        }
        if (c < d) {
            temp = c; c = d; d = temp;
            swapped = 1;
        }
        if (d < e) {
            temp = d; d = e; e = temp;
            swapped = 1;
        }
    }

    // Print after
    printf("After: %d %d %d %d %d\n", a, b, c, d, e);
}
