// test.c

#define DATA 4000
#define CODE 4500
#define STACK 400

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

int func (int a2, int b2)
{
 	int d;
 	int f = 111;
 	
 	d = a2 + b2;
 	
 	printf("d:%d\n", d);
 	printf("f:%d\n", f);
 	
	return 0;
}

int x = 111;
char *c = "AAAAAAAA";
char *c2 = "AAAAAAAA";

int main ()
{
	char *c3 = "AAAAAAAA";
	int a = 1;
	int b = 1;
	int e = 111;

 	printf("c3:%s\n", c3);
 	printf("c3:%d\n", (int)c3);

 	printf("c:%s\n", c);
 	printf("c:%d\n", (int)c);

 	printf("c2:%s\n", c2);
 	printf("c2:%d\n", (int)c2);
  	
 	c[3] = 'B';

 	printf("c:%s\n", c);
 	printf("c:%d\n", (int)c);

 	printf("c2:%s\n", c2);
 	printf("c2:%d\n", (int)c2);

 	printf("c3:%s\n", c3);
 	printf("c3:%d\n", (int)c3);
	
 	printf("x:%d\n", x);
 	printf("e:%d\n", e);
 	x = 222;
 	printf("e:%d\n", e);
 	printf("x:%d\n", x);
 	
 	printf("a:%d b:%d\n", a, b);
 	a = 2;
 	printf("a:%d b:%d\n", a, b);
 	b = 3;
 	printf("a:%d b:%d\n", a, b);
	
	func(a, b);
	
	printf("\nDone.\n");
}