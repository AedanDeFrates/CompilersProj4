union myUnion {
int[][] x;
int y;
}
// Would Type Check with element x
var myUnion ux = {{1,2},{1,2}};
// Would Type Check with element y
var myUnion uy = 1;
// Would Not Type Check
var myUnion ufails = {{{1,2},{1,2}},1};
