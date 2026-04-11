var string* p = 100;
var string s = "Example";

// Passes
var string pstring = *p;
var string* x = &s;

// Fails
//var int y = &s;
var int* z = &s;

