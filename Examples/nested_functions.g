// Minimum Grade: 90
fun int main() {

    fun void outer() {
        var int x = 10;

        fun void inner() {
            var int x = 20;   
            printf("Inner x: %d\n", x);
        }

        printf("Outer x before: %d\n", x);

        inner(); 

        printf("Outer x after: %d\n", x);
    }

    outer();
}
