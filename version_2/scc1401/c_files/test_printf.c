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
	int i = 0;

	switch(i)
	{
		case 1:
			i += 1;
			i += 2;
			break;

		case 2:
			i += 1;
			i += 2;
			break;
			
		default:
			i += 1;
			i += 3;
			break;
	}

LOOP:
	if(i < 10)
	{
		int vvv = 3;
		
		i += vvv;
		printf("%d\n", i);
		
		goto LOOP;
	}
	
	do 
	{
BEGIN:
		i++;
		
		if(i == 2)
		{
			break;
		}
		
		if(i == 3)
		{			
			i = 1 + 1;
			
			goto BEGIN;
		}
	}
	while(i < 10);
		
	while(i < 10)
	{
		i++;
		
		if(i == 5)
		{
			int a = 0;
			char *s2 = "XXXXXX";
		
			x = i;
			
			printf("%s\n", s2);
			
			break;
		}
		
		if(i == 3)
		{
			i = i;
		}
		else
		{
			continue;
		}
		
		x = i;
	}
	
	//break;
	//continue;
	
	for(i = 0; i < 10; i++)
	{
		char *aaaa;
		char *d;

		if(i == 5)
		{
			break;
		}

		if(i == 7)
		{
			int b = 100;
			
			{
				int f = 1000;
				
				{
				
					int a = 10;
				
					printf("%d\n", f + b + a + i);
				
					continue;
				}
			}
		}
		
		goto END;
		
		printf("Never printed: %d\n", i);
END:		
		x = i;		
	}

	printf("%d\n", x);
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
