fun int example(int a, string b, int[2][2] c) {
    return 1;
}

fun int main () {
    var int[2][2] x = {{2,2},{2,2}};
    var int y = example(1,"2",x);

    //fails
    var int[][] a = {{2,2},{2,2},{2,2}};
    var int b = example(1,"2",a);
}
