
#define PRINT_AREA ((char *)201)

char *__putchar_pos = PRINT_AREA;
char *__putchar_last = PRINT_AREA - 1;

int putchar(char c)
{
	if (c != '\n') {
	 *__putchar_pos++ = c;
	} else {
	 while (__putchar_last >= __putchar_pos) {
		 *__putchar_last-- = ' ';
	 }
	 __putchar_last = __putchar_pos;
	 __putchar_pos = PRINT_AREA;
	 asm("W");
	}
	if (__putchar_pos == PRINT_AREA + 132) {
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
	// this is so we can find this function in the generated code
	//asm("NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP","NOP");
	if (num < 0)
	{
		putchar('-');
		num = -num;
	}
	digits = (char*)(&num);
	digits -= 4;
	while (*digits++ == '0' && counter < 6){counter += 1;}
	if (counter >= 5)
	{
		putchar('0');
	} else {
		digits -= 1;
		while ((5 - counter) > 0){ putchar(*digits++); counter ++;}
	}
}

int printf(char *cformat_str, ...)
{
	char *arg;
	char c;
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
				putchar(*arg--);
			}
			else if (c == 's')
			{
				puts(*((char **)arg));
				arg -= sizeof(char *);
			}
			else if (c == 'd')
			{
				char a[7];
				//putnumber(*((int*)arg));
				itoa(*((int*)arg), a, 10);
				puts(a);
				arg -= sizeof(int);
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
