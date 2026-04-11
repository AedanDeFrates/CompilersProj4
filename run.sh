export CLASSPATH=$CLASSPATH:$(pwd)
export CLASSPATH=$CLASSPATH:$(pwd)/bin/
export CLASSPATH=$CLASSPATH:$(pwd)/lib/*

make clean


find . -name "*.java" > sources.txt 
javac -d bin @sources.txt

rm sources.txt

if [ "$#" -eq 0 ]; then
   java Typecheck.Main test.g

   echo "Compiling with gcc..."
   gcc test.c -o test
   echo "Executable \`test\` created. Use ./test to run"
else
   java Typecheck.Main $1

   echo "Compiling with gcc..."
   basename $1 .g
   gcc $1.c -o $1
   echo "Executable $1 created. Use ./$1 to run"
fi



