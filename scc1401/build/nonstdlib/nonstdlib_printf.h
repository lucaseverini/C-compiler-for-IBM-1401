#ifdef NON_LIB_PRINTF

char *print_area = (char*)201;
char *print_place = (char*)201;

int putc(char c)
{
	if (c == '\n' || print_place - print_area >= 132)
	{
		char *tmp;
		asm("W");
		while(print_place > print_area)
		{
			*print_place = ' ';
			print_place -= 1;
		}
		*print_place = ' ';
		if (c == '\n') return;
	}
	*print_place = c;
	print_place += 1;
	return 0;
}

int puts(char *s)
{
	char *tmp;
	tmp = s;
	while(*tmp != '\0')
	{
		putc(*tmp);
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
			if (*(cformat_str+1) == '%'){ putc('%'); cformat_str+=1; }
			else if (*(cformat_str+1) == 'c'){ putc(*arg); arg += charDelta; cformat_str+=1; }
			else if (*(cformat_str+1) == 's'){ puts(arg); arg += charDelta; cformat_str+=1; }
		} else {
			putc(*cformat_str);
		}
		cformat_str += 1;
	}

	#endif
	
