// This is a valid Geaux program
fun int fun1() {
    fun string fun2() {
        // Returns string. fun2 passes
        return "Passes";
    }
    // Returns int. fun1 passes
    return 0;
}


// Fails
fun void fun3() {
// No return statements allowed
    return 1;
}