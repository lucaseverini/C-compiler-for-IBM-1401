// test.c

#define DATA 600
#define CODE 4000
#define STACK 400

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

int x0 = 100;
int x1 = 200;
int x3 = 0;
char *c0 = "AAAAAAAA";
char *c1;
char *c2 = "AAAAAAAA";
int x2 = 1;
char *c3 = "BBBBBBBB";

int main ()
{
	int x4 = 100;
	char *c4 = "ABCDEFGH";
	
	x3 = x0 + x1;
	c1 = c0;
	c1 = c4;
	x3 = x4;

 	printf("x0:%d\n", x0);
 	printf("x1:%d\n", x1);
 	printf("x2:%d\n", x2);
 	printf("x3:%d\n", x3);
 	printf("x4:%d\n", x4);

 	printf("c0:%s\n", c0);
 	printf("c1:%s\n", c1);
	printf("c2:%s\n", c2);
	printf("c3:%s\n", c3);
	printf("c4:%s\n", c4);
	
	printf("\nDone.\n");

}