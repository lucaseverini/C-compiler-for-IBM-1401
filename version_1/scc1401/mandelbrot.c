// mandelbrot.c

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

#define SCALE 32
#define DEPTH 10

// negative arithematic has been a bit problematic,
// so we encode everything in unsigned ints

int za, zb;

int escape(int a, int b, int timeout) 
{
	int a2, b2, ab, new_a, new_b;
	
	if (timeout == 0)
	{
		return 0;
	}
	
	if (a < 0 || a > 4 * SCALE || b < 0 || b > 2 * SCALE) 
	{
		return 1;
	}
	
	a2 = a*a/SCALE - 4*a + 4*SCALE;
	b2 = b*b/SCALE -2*b + SCALE;
	ab = a*b/SCALE - 2*b - a + 2*SCALE; 
	new_a = a2 - b2 + za;
	new_b = 2*ab + zb;
	
	return escape(new_a, new_b, timeout-1);
}

int main() 
{
	for (zb = 0; zb <= 2*SCALE; ++zb) 
	{
		for (za = 0; za <= 4*SCALE; ++za) 
		{
			//if (za % 10 == 0 || zb % 10 == 0) putchar('0'); else
			putchar(escape(za, zb, DEPTH) == 0 ? 'X' : ' ');
		}
		
		putchar('\n');
	}
	
	return 0;
}
