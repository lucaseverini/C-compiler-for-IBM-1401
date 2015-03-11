// test.c

#define DATA 800
#define CODE 1000
#define STACK 400

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

int x0 = 100;
char *c0 = "AAAAAAAA";
char *c1 = "BBBBBBBB";

int main ()
{
	char *c2 = "CCCCCCCC";
	int x2 = 200;
	
 	printf("x0:%d\n", x0);
 	printf("c0:%s\n", c0);
 	printf("c1:%s\n", c1);

 	printf("x2:%d\n", x2);
 	printf("c2:%s\n", c2);
	
	printf("\nDone.\n");
}