/*	hello_world.c

	A simple C program for the IBM 1401
*/

#include "nonstdlib.h"

int main() 
{
	char *read_area = (char *)1;
	char *print_area = (char *)201;
	int i, d;	
	
	i = -20;
	d = -30;
	printf("I = %d\n", i);
	printf("D = %d\n", d);			
	
	for(i = -20; i <= 20; i++)
	{
		for(d = -20; d <= 20; d++)
		{
			printf("I = %d\n", i);
			printf("D = %d\n", d);			
			printf("I * D = %d\n", i * d);
		
			if(i < d)
			{
				printf("I < D\n");
			}
			else if(i > d)
			{
				printf("I > D\n");
			}
			else
			{
				printf("I == D\n");
			}
		
			printf("\n");
		}
	}
	
	printf("-----------\n");
	printf("Bye!!\n");
	
	return 0;
}
