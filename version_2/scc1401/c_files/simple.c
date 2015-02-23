/*
int main()
{
	int x[3];
	char y[3];
	
	int a = 4;
	int b = 5;
	int c = 4;
	
	c = a + b;

	x[0] = 11111;
	x[1] = 22222;
	x[2] = 33333;

	y[0] = 'A';
	y[1] = 'B';
	y[2] = 'C';

	return 0;
}
*/

#include <nonstdlib.h>

int func()
{
	char a[7];
	
	a[0] = 'a';
	a[1] = '\0';
	
	printf("%s\n", a);
}

int main()
{
	func();

	return 0;
}
