
#include <nonstdlib.h>

#define DISKS 10

int towers (int n, char frompeg, char topeg, char auxpeg)
{
	if(n == 1)
	{ 
		printf("Move disk 1 from peg %c to peg %c\n", frompeg, topeg);
		
	    return;
	}
	  
	/* Move top n-1 disks from A to B, using C as auxiliary */
	towers(n - 1, frompeg, auxpeg, topeg);
	  
	/* Move remaining disks from A to C */
	printf("Move disk %d from peg %c to peg %c\n", n, frompeg, topeg);
	
	/* Move n-1 disks from B to C using A as auxiliary */
	towers(n - 1, auxpeg, topeg, frompeg);
	
	// If not specified a return of 0 is assumed
}

int main ()
{ 
	int n;
	n = (int)getchar();
	printf("Number of disks : %d\n", n);
	
	printf("The Tower of Hanoi involves the moves :\n");
	towers(n, 'A', 'C', 'B');
	
	printf("Done.\n");
	
	return 0; 	// Not necessary
}
