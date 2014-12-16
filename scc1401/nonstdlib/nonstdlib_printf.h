#ifdef NON_LIB_PRINTF


#define PRINT_AREA ((char *)201)

char *__putchar_pos = PRINT_AREA;
char *__putchar_last = PRINT_AREA - 1;

int putchar(char c)
{
	if (c != '\n') {
	 *__putchar_pos++ = c;
	} else {
	 while (__putchar_last > __putchar_pos) {
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
	char *tmp;
	tmp = s;
	while(*tmp != '\0')
	{
		putchar(*tmp);
		tmp += 1;
	}
}

int printf(char *cformat_str, ...)
{
	char *arg;
	char *charDelta = (char *) 15999;
	char *intDelta = (char *) 15995;
	arg = cformat_str + charDelta;
	while(*cformat_str != '\0')
	{
		if (*cformat_str == '%')
		{
			if (*(cformat_str+1) == '%'){ putchar('%'); cformat_str+=1; }
			else if (*(cformat_str+1) == 'c'){ putchar(*arg); arg += charDelta; cformat_str+=1; }
			else if (*(cformat_str+1) == 's'){ puts(arg); arg += charDelta; cformat_str+=1; }
		} else {
			putchar(*cformat_str);
		}
		cformat_str += 1;
	}
}

#endif
