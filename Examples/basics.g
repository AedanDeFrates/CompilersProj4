// Minimum Grade: 75 
fun int main() {
    var int x = 10;
    var int y = 5;
    var int z = 0;

    z = x + y;
    printf("Sum: %d\n", z);

    if (z < 11) {
        printf("not greater\n");
    } else {
        printf("greater\n");
    }

    if (y < x) {
        printf("x wins\n");
    }
}
