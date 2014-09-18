build:
	javac Java.java Pascal.java

run_java: build
	java Java compile javatest.in

run_pas: build
	java Pascal compile hello.pas

all: build

clean:
	find . -name "*.class" -type f -delete
