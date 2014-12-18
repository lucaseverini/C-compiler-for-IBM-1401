#define NON_LIB_PRINTF
#define NON_LIB_READ
#include <nonstdlib.h>

int main() {
	char *tmp = "fail whale\n";
	char c;
	c = getchar();
	printf("%c\n",c);
	printf("%c testing %s\n",'f',tmp);
	c = getchar();
	printf("%c\n",c);
}

