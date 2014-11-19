JAVACC="$(HOME)/bin/javacc-6.0"

all: 
	mkdir temp; \
	make compile; \
	rm -r temp;

javacc: jjtree
	mkdir temp/preprocessor; \
	mkdir temp/compiler; \
	cd temp/preprocessor; \
	java -classpath "$(JAVACC)/bin/lib/javacc.jar" javacc ../../src/preprocessor/small_c_preprocessor.jj; \
	cp ../../src/preprocessor/*.java .; \
	cd ../compiler; \
	java -classpath "$(JAVACC)/bin/lib/javacc.jar" javacc ../../src/compiler/small_c.jj; \
	cd ..;

jjtree:
	#cd temp; \
	#java -classpath "$(JAVACC)/bin/lib/javacc.jar" jjtree ../src/compiler/small_c.jjt; \
	#cd ..;

compile: javacc
	javac  -classpath temp temp/preprocessor/SmallCPP.java -d build; \
	javac  -classpath temp temp/compiler/SmallCC.java -d build;
