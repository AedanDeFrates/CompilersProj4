    fun int main(){
    var string s = "Hello";
    var int x = 3;

    //Succeeds
    if (1+2) {}
    if (x) {}
    if (x+1){}
    if (x<3){}

    // Fails
    if (x<s){}
    if (s){}
    if ("hello") {}
}