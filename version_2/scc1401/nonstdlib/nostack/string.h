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

int atoi(char *str)
{
	char *p;
	int val = 0;
	int mul = 1;
	int idx = 0;
	int neg = 0;
	
	if(str[0] == '-')
	{
		neg = 1;
		str++;
	}
	
	p = str + strlen(str) - 1;
	while (p >= str && idx++ < 5)
	{
		val += ((int)*p) * mul;
		mul *= 10;
		p--;
	}
	
	if(neg)
	{
		val *= -1;
	}
	
	return val;
}
