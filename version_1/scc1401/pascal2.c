// pascal2.c
// Pascal Triangle - second version

#define DATA 1000
#define CODE 1600
#define STACK 400

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

/*
#define ITERATIONS 20

int pascals(int *x, int *y, int d)
{
	int i;
	int v;
	char c;
	
	for (i = 1; i < d; i++)
	{
		// v = y[i] = x[i - 1] + x[i];
		// c = i < d - 1 ? ' ' : '\n';
		// printf("%d%c", v, c);
		
		printf("%d%c", y[i] = x[i - 1] + x[i], i < d - 1 ? ' ' : '\n');
	}
 
	return ITERATIONS >= d ? pascals(y, x, d + 1) : 0;
}
*/
int main()
{
/*
	int x[ITERATIONS + 1];
	int y[ITERATIONS + 1];
	int i;
*/
	printf("\nDone.\n");
/*	
	printf("Pascal Triangle with %d iterations\n\n", ITERATIONS);

	for(i = 0; i <= ITERATIONS; i++)
	{
		x[i] = i == 1 ? 1 : 0;
		y[i] = 0;
	}
	
    pascals(x, y, 0);
    
	printf("\nDone.\n");
*/
}