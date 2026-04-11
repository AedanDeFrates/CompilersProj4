var string* p = 8; // Passes
var int x = p; // Passes
var int y = p + x; // Passes

var int z = "Hello World"; // Throws Error
var string s = 0; // Throws Error