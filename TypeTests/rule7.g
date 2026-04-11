
// Passes because [][] can be instantiated with [2][2] to match the list
var int[][] x = {{1,2},{1,2}}; // int[2][2] x = {{1,2},{1,2}}

// Fails because no x,y exists to fill [][] to match the list
var int[][] y = {{1,2,3},{1,2}};