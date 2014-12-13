#ifndef NON_STD_LIB_PRINTF
#define NON_STD_LIB_PRINTF

#ifdef NON_LIB_PRINTF

#ifdef C_STRINGS
#define printf cprintf
#endif

#ifdef P_STRINGS
#define printf pprintf
#endif

int pprintf(char *pformat_str, ...)
{
	char *arg;
	int size_of_str;
	int index = 0;
	char *delta = (char *)15995;
	arg = pformat_str + delta;
	size_of_str = *pformat_str;
	while(index < size_of_str)
	{
		if (pformat_str[index] == '%')
		{
			if (pformat_str[index+1] == '%'){ putc('%'); index++; }
				else if(*(cformat_str+1) == 'd'){ prtis(*((int)arg)); arg += delta; index++; }
				else if (*(cformat_str+1) == 'c'){ putc(*arg); arg += delta; index++; }
			} else {
				putc(pformat_str[index]);
			}
			index ++;
		}
	}

int cprintf(char *cformat_str, ...)
{
	char *arg;
	// delta is the int value of the char ptr to -1
	// this allows us to speed up the pointer maths as
	// addition is faster than subtraction
	char *delta = (char *)15995;
	arg = cformat_str + delta;

	while(*cformat_str != 0)
	{
		if (*cformat_str == '%')
		{
			if (*(cformat_str+1) == '%'){ putc('%'); cformat_str++; }
				else if(*(cformat_str+1) == 'd'){ prtis(*((int)arg)); arg += delta; cformat_str++; }
				else if (*(cformat_str+1) == 'c'){ putc(*arg); arg += delta; cformat_str++; }
		} else {
			putc(*cformat_str);
		}
		cformat_str ++;
	}
}
#endif

#endif
