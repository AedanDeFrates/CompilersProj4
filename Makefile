JFLAGS=-g
SHELL=/bin/bash

Parse:
	javac -g **/*.java

clean:
	rm -rf ./bin;
	rm -rf Parse/antlr_build;
