JAVACC=$(HOME)/bin/javacc-6.0

all: 
	mkdir -p temp/compiler; \
	mkdir -p temp/preprocessor; \
	mkdir -p temp/wci/intermediate; \
	mkdir -p temp/wci/util; \
	cp -r src/wci temp; \
	make compile; \
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
	javac -classpath temp temp/preprocessor/SmallCPP.java -d build; \
	javac -classpath temp temp/wci/intermediate/*.java -d build; \
	javac -classpath temp temp/wci/util/*.java -d build; \
	javac -classpath temp temp/compiler/SmallCC.java -d build;
