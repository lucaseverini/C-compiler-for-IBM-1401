// test.c

// #define STACK 400
#define DATA 600
// #define CODE 800

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

char *c1 = "0123456789ABCDEF";
/*
int pppp (char c)
{
	char x = 'Z';
	x = c;
}
*/

int test(char* ptr, int pos)
{
	if(pos == 1)
	{
		if(pos == 0)
		{
			pos = 5;
		}
		else
		{
			pos = 4;
		}
	}
	else
	{
		if(pos == 2)
		{
			pos = 6;
		}
		else
		{
			pos = 7;
		}
	}

	if(pos == 7)
	{
		return 1;
	}
	else if(pos == 6)
	{
		return 2;
	}
	
	pos = 10;
	
	return pos;
}

int main ()
{
	char *c;
	int x;
	
	c = c1 + 10;	
	x = *(int*)c;
	
	x = test(c, 1);

	printf("c1: %d\n", (int)c1);
	printf("*c1: %c\n", *c1);
	printf("c: %d\n", (int)c);
	printf("*c: %c\n", *c);
	printf("x: %d\n", x);
	printf("&x: %d\n", (int)&x);

/*
	char A = '-';
	char *c;
	char B = '-';
	char x;
	char C = '-';
	int y = 12345;
	char D = '-';
	int cc;
	char E = '-';
	int *ii = 200;
	char F = '-';
	char *d = 450;
	
	ii = (int*)c1;
	ii += 10;
	
	cc = *(ii + 1);
	printf("cc: %c\n", (char)(*(ii + 1)));
	
	cc = *(ii - 1);
	printf("cc: %c\n", (char)(*(ii + 1)));
	
	c = c1;
	c += 10;
	
	cc = (int)*(c - 1);
	x = *(c - 1);
	
	pppp((char)(*(c - 3)));

	printf("c-1: %c\n", *(c - 1));
	printf("c+1: %c\n", *(c + 1));
	printf("cc: %d\n", cc);

	printf("x: %c\n", *(c - 1));
	printf("x: %c\n", *(c - 2));

	printf("x: %c\n", *c);
	printf("x: %c\n", *(c + 1));
	printf("x: %c\n", *(c + 2));
	printf("x: %c\n", *(c += 10));
	printf("x: %c\n", *(c - 1));
	printf("x: %c\n", *(c - 2));
	
	d = (char*)&c;
	d = (char*)(c1+10);
	printf("d as ptr: %d\n", d);

	printf("d: %c\n", *d);
	printf("d: %c\n", *(d - 1));
	printf("d: %c\n", *(d - 2));

	//x = *d;
	//x2 = *(d - 1);
	//x3 = *(d - 2);
	//d -= (char*)2;
	//printf("d address: %c%c%c\n", *(d++), *(d++), *d);

	printf("*c1: %c\n", *c1);
	printf("c: %d\n", c);
	printf("d: %d\n", d);
	printf("d: %d\n", d - 1);
	printf("d: %d\n", d - 2);
	printf("y: %d\n", y);
	printf("x: %c\n", *d);
	printf("x: %c\n", *(d + 1));
	printf("x: %c\n", *(d + 2));
*/
/*	
	char *c = 6034;
	int x = 9999;
	char a = 'C';
	char b = 'A';
	
	printf("c: %p\n", c);
	putchar(a);
	putchar(b);
	putchar('\n');

	printf("c1: %d\n", (int)c1);
	printf("c1: %d\n", c1);
	
	printf("c: %d\n", (int)c);
	printf("c: %d\n", c);
	printf("c: %p\n", c);

	// integers, pointers and chars can't be intermixed freely if cformat_str prints more than one argument 
	// Needs to implement varsize() which returns the real length of the variable (1, 3 or 5)
	//printf("x: %d %d\n", x, 100);
	//printf("x: %d %d\n", (char*)x, 110);
	
	printf("c1: %p\n", c1);			// should print in 3-digit format
	printf("x: %p\n", (char*)x);	// should print in 3-digit format

	printf("a: %c\n", a);

	printf("A %d\n", (int)'A');
	printf("B %d\n", (int)'B');
	printf("C %d\n", (int)'C');
	printf("D %d\n", (int)'D');
	printf("E %d\n", (int)'E');
	printf("F %d\n", (int)'F');
	printf("G %d\n", (int)'G');
	printf("H %d\n", (int)'H');
	printf("I %d\n", (int)'I');
	printf("J %d\n", (int)'J');
	printf("K %d\n", (int)'K');
	printf("L %d\n", (int)'L');
	printf("M %d\n", (int)'M');
	printf("N %d\n", (int)'N');
	printf("O %d\n", (int)'O');
	printf("P %d\n", (int)'P');

	printf("Q %d\n", (int)'Q');

	printf("R %d\n", (int)'R');
	printf("S %d\n", (int)'S');
	printf("T %d\n", (int)'T');
	printf("U %d\n", (int)'U');
	printf("V %d\n", (int)'V');
	printf("W %d\n", (int)'W');
	printf("X %d\n", (int)'X');
	printf("Y %d\n", (int)'Y');
	printf("Z %d\n", (int)'Z');

	printf("a: %d\n", (int)a);
	printf("a: %p\n", (char*)a);
	
	//x = (int)'A';	// put 65
	//a = (char)'A';
	x = (int)a;

	printf("a: %c\n", a);
	printf("a: %d\n", a);
	printf("x: %d\n", x);

	x = (int)b;
	a = (char)x;
	printf("a: %c\n", a);
	printf("a: %d\n", a);
	printf("x: %d\n", x);

	printf("x: %d\n", x);
	x = (int)'B';
	printf("x: %d\n", x);
	x = (int)'C';
	printf("x: %d\n", x);
	x = (int)'D';
	printf("x: %d\n", x);
	x = (int)'E';
	printf("x: %d\n", x);
	x = (int)'F';
	printf("x: %d\n", x);
	x = (int)'G';
	printf("x: %d\n", x);
	x = (int)'H';
	printf("x: %d\n", x);
	x = (int)'I';
	printf("x: %d\n", x);
	x = (int)'L';
	printf("x: %d\n", x);
	x = (int)'M';
	printf("x: %d\n", x);
	x = (int)'N';
	printf("x: %d\n", x);
	x = (int)'O';
	printf("x: %d\n", x);
	x = (int)'P';
	printf("x: %d\n", x);
	x = (int)'Q';
	printf("x: %d\n", x);
*/
}