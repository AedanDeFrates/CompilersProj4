// Minimum Grade: 100
fun int main() {
    var string path = "test.txt";
    var string content = "Hello, file system!\n";
    var string result;

    writeToFile(path, content);

    result = readFromFile(path);

    printf("%s", result);
}
