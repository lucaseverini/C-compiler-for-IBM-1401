
#define PRINT_AREA ((char *)200)

char *__putchar_pos = PRINT_AREA;

int print_start()
{
	__putchar_pos = PRINT_AREA;
}

int print_num(int n) {
	int* p;
	__putchar_pos += sizeof(int);
	p = (int* )__putchar_pos;
	*p = n;

}


int print_char(char c) {
	char* p;
	__putchar_pos += sizeof(char);
	p = __putchar_pos;
	*p = c;
}

int print_str(char *str) {
	char* p;
	p = __putchar_pos;
	while(*str != '\0')
	{
		if (*str == '\n')
		{
			asm("W");
			asm("CS   299");
			asm("CS   332");
			__putchar_pos = PRINT_AREA;
			p = __putchar_pos;
		} else {
			__putchar_pos += sizeof(char);
			p += sizeof(char);
			*p = *str;
		}
		str ++;
	}
}

int print_finish()
{
	asm("W");
	asm("CS   299");
	asm("CS   332");
	__putchar_pos = PRINT_AREA;
}

#undef PRINT_AREA
