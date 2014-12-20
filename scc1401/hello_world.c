/*	hello_world.c

	A trivial C program for the IBM 1401
*/

#include <nonstdlib.h>

int main() 
{
	char *read_area = (char *)1;
	char *print_area = (char *)201;
	char *hello =  "Hello World";
	char *hello2 = "Sean Papay";
	char *hello3 = "Matt Pleva";
	char *hello4 = "Luca Severini";
	
	printf("%s %s %s %s\n", hello, hello2, hello3, hello4);
	printf("-----------\n");
	printf("Bye!!\n\n", hello, hello2, hello3, hello4);
	
	int i;	
	for (i = 0; i < 11; ++i)
	{
		print_area[i] = hello[i];
	}
	
	asm("W");
	asm("CS   299");
	
	for (i = 0; i < 10; ++i)
	{
		print_area[i] = hello2[i];
	}

	asm("W");
	asm("CS   299");
	
	for (i = 0; i < 10; ++i)
	{
		print_area[i] = hello3[i];
	}

	asm("W");
	asm("CS   299");

	for (i = 0; i < 13; ++i)
	{
		print_area[i] = hello4[i];
	}

	asm("W");
	asm("CS   299");
	
	asm("R");
	
	for (i = 0; i <= 80; ++i)
	{
		print_area[i] = read_area[i];
	}

	asm("W");
	asm("CS   299");
	
	return 0;
}
