export CLASSPATH=$CLASSPATH:$(pwd)
export CLASSPATH=$CLASSPATH:$(pwd)/bin/
export CLASSPATH=$CLASSPATH:$(pwd)/lib/*

make clean


find . -name "*.java" > sources.txt 
javac -d bin @sources.txt

rm sources.txt

if [ "$#" -eq 0 ]; then
   java Typecheck.Main test.g
else
   java Typecheck.Main $1
fi
