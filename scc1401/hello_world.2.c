
char *__putchar_pos = ((char *)201);
char *__putchar_last = ((char *)201) - 1;
int putchar(char c)
{
	if (c != '\n') {
	 *__putchar_pos++ = c;
	} else {
	 while (__putchar_last >= __putchar_pos) {
		 *__putchar_last-- = ' ';
	 }
	 __putchar_last = __putchar_pos;
	 __putchar_pos = ((char *)201);
	 asm("W");
	}
	if (__putchar_pos == ((char *)201) + 132) {
		__putchar_last = __putchar_pos;
		__putchar_pos = ((char *)201);
		asm("W");
	}
}
int puts(char *s)
{
	while(*s != '\0')
	{
		putchar(*s++);
	}
}
void printf(char *cformat_str, ...)
{
	char *arg;
	char c;
	arg = (char *)(&cformat_str - 1);
	while ((c = *cformat_str++) != '\0') {
		if (c != '%') putchar(c);
		else {
			c = *cformat_str++;
			if (c == '%') {
				putchar('%');
			} else if (c == 'c') {
				putchar(*arg--);
			} else if (c == 's') {
				puts(*((char **)arg));
				arg -= sizeof(char *);
			} else if (c == 'd') {
				
			} else return;
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
	while (n--) *dest++ = *src++;
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
	if (value < 0) {
		*str++ = '-';
		value = -value;
	} else if (value == 0) {
		str[0] = '0';
		str[1] = '\0';
		return start;
	}
	while (exp < value/base) exp *= base;
	while (exp) {
		*str++ = digits[value / exp];
		value %= exp;
		exp /= base;
	}
	*str = '\0';
	return start;
}
int main() 
{
	char *read_area = (char *)1;
	char *print_area = (char *)201;
	char *hello =  "Hello World";
	char *hello2 = "Sean Papay";
	char *hello3 = "Matt Pleva";
	char *hello4 = "Luca Severini";
	
	printf("%s %s %s %s\n", hello, hello2, hello3, hello4);
	printf("-----------\n");
	printf("Bye!!\n\n", hello, hello2, hello3, hello4);
	
	int i;	
	for (i = 0, d = 0; i < 11; ++i)
	{
		print_area[i] = hello[i];
	}
	
	asm("W");
	asm("CS   299");
	
	for (i = 0; i < 10; ++i)
	{
		print_area[i] = hello2[i];
	}
	asm("W");
	asm("CS   299");
	
	for (i = 0; i < 10; ++i)
	{
		print_area[i] = hello3[i];
	}
	asm("W");
	asm("CS   299");
	for (i = 0; i < 13; ++i)
	{
		print_area[i] = hello4[i];
	}
	asm("W");
	asm("CS   299");
	
	asm("R");
	
	for (i = 0; i <= 80; ++i)
	{
		print_area[i] = read_area[i];
	}
	asm("W");
	asm("CS   299");
	
	return 0;
}
