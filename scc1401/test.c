int foo = 5, **bar[3*2];

int *hello(int a, int b, int c);

int *hello(int a, int b, int c) {
	int foo;
	int bar;
	return a + b * c;
}
