#define NON_LIB_PRINTF
#include <nonstdlib.h>
int main() {
	int a = 0;
	while (a++ < 10)
	{
		printf("Hello world! %d", a);
	}
}
