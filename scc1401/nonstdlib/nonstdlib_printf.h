#ifdef NON_LIB_PRINTF

char *print_area = (char*)201;
char *print_place = (char*)201;

int putc(char c)
{
	if (c == '\n')
	{
		char *tmp;
		tmp = print_area;
		asm("W");
		while(tmp-print_area < 131)
		{
			*tmp++ = ' ';
		}
		print_place = print_area;
	}
	if (print_place - print_area >= 132) {
		char *tmp;
		tmp = print_area;
		asm("W");
		while(tmp-print_area < 131)
		{
			*tmp++ = ' ';
		}
		print_place = print_area;
		*print_place++ = c;
	}
	if (print_place - print_area < 132)
	{
		*print_place++ = c;
	}
	return 0;
}

int puts(char *s)
{
	char *tmp;
	tmp = s;
	while(*tmp != '\0')
	{
		putc(*tmp++);
	}
}

int printf(char *cformat_str, ...)
{
	char *arg;
	char *charDelta = (char *) 15999;
	char *intDelta = (char *) 15995;
	arg = cformat_str + charDelta;
	while(*cformat_str != 0)
	{
		if (*cformat_str == '%')
		{
			if (*(cformat_str+1) == '%'){ putc('%'); cformat_str++; }
				else if (*(cformat_str+1) == 'c'){ putc(*arg); arg += charDelta; cformat_str++; }
				else if (*(cformat_str+1) == 's'){ puts(arg); arg += charDelta; cformat_str++; }
			} else {
				putc(*cformat_str);
			}
			cformat_str ++;
		}
	}

	#endif
	
