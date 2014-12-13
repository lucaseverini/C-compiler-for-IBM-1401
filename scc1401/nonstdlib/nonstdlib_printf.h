#ifdef NON_LIB_PRINTF

int cprintf(char *cformat_str, ...)
{
	char *arg;
	// delta is the int value of the char ptr to -1
	// this allows us to speed up the pointer maths as
	// addition is faster than subtraction
	char *charDelta = (char *) 15999;
	char *intDelta = (char *) 15995;
	arg = cformat_str + charDelta;

	while(*cformat_str != 0)
	{
		if (*cformat_str == '%')
		{
			if (*(cformat_str+1) == '%'){ putc('%'); cformat_str++; }
				else if(*(cformat_str+1) == 'd'){ prtis(*((int)arg)); arg += intDelta; cformat_str++; }
				else if (*(cformat_str+1) == 'c'){ putc(*arg); arg += charDelta; cformat_str++; }
				else if (*(cformat_str+1) == 's'){ puts(*arg); arg += charDelta; cformat_str++; }
		} else {
			putc(*cformat_str);
		}
		cformat_str ++;
	}
}

#endif
