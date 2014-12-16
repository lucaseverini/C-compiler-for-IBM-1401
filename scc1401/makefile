#!/bin/sh
#
#	makefile
#
#	Small-C compiler
#   CS153 Final project - SJSU
#	November-8-2014
#
#	By Sean Papay, Matt Pleva, Luca Severini
#
#	TODO add these back into the compile tag
#

JAVACC=$(HOME)/bin/javacc-6.0

all:
	mkdir -p build; \
	mkdir -p temp/compiler; \
	mkdir -p temp/preprocessor; \
	mkdir -p temp/wci/intermediate; \
	mkdir -p temp/wci/util; \
	cp -r src/* temp; \
	make compile; \
	#rm -r temp; \
	cp -r snippets build; \
	cp -r nonstdlib build; \
	rm -r temp;

javacc: jjtree
	cd temp/preprocessor; \
	java -classpath $(JAVACC)/bin/lib/javacc.jar javacc ../../src/preprocessor/small_c_preprocessor.jj; \
	cp ../../src/preprocessor/*.java .; \
	cd ../compiler; \
	java -classpath $(JAVACC)/bin/lib/javacc.jar javacc small_c.jj; \
	cd ..;

jjtree:
	cd temp/compiler; \
	java -classpath $(JAVACC)/bin/lib/javacc.jar jjtree ../../src/compiler/small_c.jjt; \
	cd ../..;

compile: javacc
	javac -classpath temp temp/preprocessor/SmallC_PreProcessor.java -d build; \
	javac -classpath temp temp/retree/exceptions/*.java -d build; \
	javac -classpath temp temp/retree/expression/*.java -d build; \
	javac -classpath temp temp/retree/symtab/*.java -d build; \
	javac -classpath temp temp/retree/type/*.java -d build; \
	javac -classpath temp temp/retree/*.java -d build; \
	javac -classpath temp temp/retree/program/*.java -d build; \
	javac -classpath temp temp/retree/statement/*.java -d build; \
	javac -classpath temp temp/compiler/*.java -d build;
