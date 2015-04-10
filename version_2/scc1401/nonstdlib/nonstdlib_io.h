
#define PRINT_AREA ((char *)201)

char *__putchar_pos = PRINT_AREA;
char *__putchar_last = PRINT_AREA - 1;

int putchar(char c)
{
	if (c != '\n') 
	{
	 *__putchar_pos++ = c;
	}
	else 
	{
		while (__putchar_last >= __putchar_pos) 
	 	{
		 *__putchar_last-- = ' ';
	 }
	 	
	 __putchar_last = __putchar_pos;
	 __putchar_pos = PRINT_AREA;
	 	
	 asm("W");
	}
	
	if (__putchar_pos == PRINT_AREA + 132)
	{
		__putchar_last = __putchar_pos;
		__putchar_pos = PRINT_AREA;
		
		asm("W");
	}
}
#undef PRINT_AREA

int puts(char *s)
{
	while(*s != '\0')
	{
		putchar(*s++);
	}
}

int putnumber(int num)
{
	char *digits;
	int counter = 0;

	if (num < 0)
	{
		putchar('-');
		num = -num;
	}
	
	digits = (char*)(&num);
	digits -= 4;
	while (*digits++ == '0' && counter < 6)
	{
		counter += 1;
	}
	
	if (counter >= 5)
	{
		putchar('0');
	} 
	else 
	{
		digits -= 1;
		
		while ((5 - counter) > 0)
		{
		 	putchar(*digits++); counter ++;
		}
	}
}

int printf(char *cformat_str, ...)
{
	// NOTE
	// integers, pointers and chars can't be intermixed freely if cformat_str prints more than one argument 
	// Needs to implement varsize() which returns the real length of the variable (1, 3 or 5)
	
	char *arg;
	char c;
	char *addrP;
	char addrC;
	
	arg = (char *)(&cformat_str - 1);
	while ((c = *cformat_str++) != '\0')
	{
		if (c != '%')
		{
			putchar(c);
		}
		else
		{
			c = *cformat_str++;
			
			if (c == '%')
			{
				putchar('%');
			}
			else if (c == 'c')
			{
				putchar(*arg);
				arg -= sizeof(char);
			}
			else if (c == 's')
			{
				puts(*((char **)arg));
				arg -= sizeof(char*);
			}
			else if (c == 'd')					// Prints 5-digit integer 
			{
				char a[7];
				itoa(*((int*)arg), a, 10);
				puts(a);
				arg -= sizeof(int);
			}
			else if (c == 'p')					// Prints 3-digit pointers
			{
				addrP = (char*)arg;
				addrC = *addrP;
				putchar(addrC);
				
				asm("W");
				asm("W");
				asm("W");
				arg -= sizeof(char);
				//char a[4];
				//itoa(*((int*)arg), a, 10);
				//puts(a);
				//arg -= sizeof(char*);
			}
			else
			{
				return;
			}
		}
	}
}

char *__getCharPosition = (char *)81;

char getchar()
{
	if (__getCharPosition > (char *)80)
	{
		asm("R");
		__getCharPosition = (char *)1;
	}
	
	return *__getCharPosition++;
}
