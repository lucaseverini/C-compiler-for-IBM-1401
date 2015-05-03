// test.c

// #define STACK 400
#define DATA 700
// #define CODE 800

#ifdef IBM1401
	#include <nonstdlib.h>
#else
	#include <stdio.h>
#endif

// char str[30];
int x = 0;

int main ()
{
	int i = 4;

	switch(++i)
	{
		case 1:
			i += 1;
			break;

		case 2:
			i += 2;
			break;
			
		case 3:
			i += 3;
			break;

		default:
			i += 4;
			break;
	}

	printf("%d\n", i);
	
	while(i < 20)
	{
		printf("%d\n", i++);
		
		if(i == 15)
		{
			break;
		}
	}
	
	printf("%d\n", i);
/*
	char *num = "12345";

	int i = -123;
	int i1 = 1;
	int i2 = 100;
	int i3 = 10000;
	
	char *s1 = "String1";
	char *s2 = "String2";
	char *s3 = "String3";
	
	char c1 = 'A';
	char c2 = 'B';
	char c3 = 'C';
	
	printf("## i: %d\n", i);

	i = (int)num[1];
	i1 = (int)num[2];
	i2 = i * i1;
	printf("i: %d\n", i);
	printf("i1: %d\n", i1);
	printf("i2: %d\n", i2);
	
	printf("i: %d\n", atoi("12345"));
	printf("i: %d\n", atoi("1"));
	printf("i: %d\n", atoi("12"));
	printf("i: %d\n", atoi("-123"));
	printf("i: %d\n", atoi("1234"));
	printf("i: %d\n", atoi("99999"));
	printf("i: %d\n", atoi("987654321"));
	printf("i: %d\n", atoi(""));
	
	printf("%s-%s-%s\n", s1, s2, s3);
	printf("%d-%d-%d\n", i1, i2, i3);
	printf("%c-%c-%c\n", c1, c2, c3);
	
	printf("===============\n");

	sprintf(str, "%s-%s-%s", s1, s2, s3);
	printf("%s\n", str);
	sprintf(str, "%d-%d-%d", i1, i2, i3);
	printf("%s\n", str);
	sprintf(str, "%c-%c-%c", c1, c2, c3);
	printf("%s\n", str);
	
	printf("===============\n");

	printf("%d\n", str);
	
	printf("===============\n");

	// printf("%p\n", str);		// Crash
	printf("%d\n", (int)str);
	
	printf("===============\n");

	i3 = (int)str;
	printf("int   : %d\n", i3);
	printf("char* : %d\n", (char*)i3);
*/
}
