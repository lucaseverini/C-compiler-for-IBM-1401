/* Fibonacci Series c language */

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

int main()
{
	unsigned int n, c, next, first = 0, second = 1;
 
	// printf("Enter the number of terms\n");
	// scanf("%d",&n);
	n = 48;
 
	printf("First %d terms of Fibonacci series are :\n", n);
 
	for(c = 0; c < n ;c++)
	{
		if (c <= 1)
		{
			next = c;
		}
		else
		{
			next = first + second;
			first = second;
			second = next;
		}
		
		printf("%u\n", next);
	}
 
	return 0;
}