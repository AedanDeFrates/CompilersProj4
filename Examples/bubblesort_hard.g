// Minimum Grade: 100
var int[] arr;

fun void swap(int i, int j) {
    var int x;   
    x = arr[i];
    arr[i] = arr[j];
    arr[j] = x;
}

fun void bubbleSort(int n) {
    var int x = 1;   
    var int j;

    while (!(x < 1)) {
        x = 0;
        j = 0;

        while ((j+1) < n) {
            if (arr[j+1] < arr[j]) {
                swap(j, j + 1);
                x = 1;
            }
            j = j + 1;
        }
    }
}

fun int main() {
    var int x = 0;   

    while (x < 5) {
        arr[x] = input();
        // alternative: input(arr[x]);
        x = x + 1;
    }

    x = 0;
    printf("Before: ");
    while (x < 5) {
        printf("%d ", arr[x]);
        x = x + 1;
    }
    printf("\n");

    bubbleSort(5);

    x = 0;
    printf("After: ");
    while (x < 5) {
        printf("%d ", arr[x]);
        x = x + 1;
    }
    printf("\n");
}
