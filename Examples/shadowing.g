// Minimum Grade: 85
var int x = 10;

fun void test() {
    var int x = 10;

    printf("Inside test (start): %d\n", x);

    if (x < 15) {
        var int x = 30;  
        printf("Inside if block: %d\n", x);
    }

    printf("Inside test (end): %d\n", x);
}

fun int main() {
    printf("Global before: %d\n", x);

    test();

    printf("Global after: %d\n", x);
}
