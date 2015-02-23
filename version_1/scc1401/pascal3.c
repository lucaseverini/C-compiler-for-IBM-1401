// pascal2.c
// Pascal Triangle - second version

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

#define D 10

int pascals(int *x, int *y, int d)
{
	int i;
	for (i = 1; i < d; i++)
	{
		y[i] = x[i - 1] + x[i];
		printf("%d", y[i]);
		
		if(i < d - 1)
		{
			printf(" ");
		}
		else
		{
			printf("\n");
		}
	}
	
	if(D > d)
	{
		i = pascals(y, x, d + 1);
		return i;
	}
	else
	{
		return 0;
	}
}

int main()
{
	int x[D];
	int y[D];
	
	x[0] = 0;
	x[1] = 1;
	x[2] = 0;
	x[3] = 0;
	x[4] = 0;
	x[5] = 0;
	x[6] = 0;
	x[7] = 0;
	x[8] = 0;
	x[9] = 0;
	
	y[0] = 0;
	y[1] = 0;
	y[2] = 0;
	y[3] = 0;
	y[4] = 0;
	y[5] = 0;
	y[6] = 0;
	y[7] = 0;
	y[8] = 0;
	y[9] = 0;
	
	pascals(x, y, 0);
	
	return 0; 	// Not necessary
}