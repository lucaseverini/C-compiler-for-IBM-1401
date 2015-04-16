int strlen(char *str)
{
	int len = -1;
	while (str[++len] != '\0');
	return len;
}

int strcpy(char *dest, char *src)
{
	while ((*dest++ = *src++) != '\0');
}

int strncpy(char *dest, char *src, int n)
{
	while (n-- && (*dest++ = *src++) != '\0');
}

int memcpy(char *dest, char *src, int n)
{
	while (n--)
	{
		*dest++ = *src++;
	}
}

int strcat(char *dest, char *src)
{
	strcpy(dest + strlen(dest), src);
}

char *itoa(int value, char *str, int base)
{
	char *start, *digits = "0123456789ABCDEF";
	int exp = 1;
	start = str;
	
	if (value < 0)
	{
		*str++ = '-';
		value = -value;
	}
	else if (value == 0)
	{
		str[0] = '0';
		str[1] = '\0';
		return start;
	}
	
	while (exp <= value/base)
	{
		exp *= base;
	}
	
	while (exp)
	{
		*str++ = digits[value / exp];
		value %= exp;
		exp /= base;
	}
	
	*str = '\0';
	
	return start;
}

/*
int sprintf(char *str, char *cformat_str, ...)
{
	// NOTE
	// integers, pointers and chars can't be intermixed freely if cformat_str prints more than one argument 
	// Needs to implement varsize() which returns the real length of the variable (1, 3 or 5)
	
	char *arg;
	char c;
	
	arg = (char *)(&cformat_str - 1);
	while ((c = *cformat_str++) != '\0')
	{
		if (c != '%')
		{
			*str++ = c;
		}
		else
		{
			c = *cformat_str++;
			
			if (c == '%')
			{
				*str++ = '%';
			}
			else if (c == 'c')
			{
				*str++ = *arg;
				
				arg -= sizeof(char);
			}
			else if (c == 's')
			{
				char *s;
				s = *((char **)arg)
				while(*s != '\0')
				{
					*str++ = *s;
				}

				arg -= sizeof(char*);
			}
			else if (c == 'd')					// Prints 5-digit integer 
			{
				char a[7];
				itoa(*((int*)arg), a, 10);

				while(*a != '\0')
				{
					*str++ = *a;
				}
				
				arg -= sizeof(int);
			}
			else
			{
				return;
			}
		}
	}
}
*/