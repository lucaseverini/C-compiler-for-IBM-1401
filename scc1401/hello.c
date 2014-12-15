#define NON_LIB_PRINTF
#include <nonstdlib.h>

int main() {
	char format_str[12]
	format_str[0] = 'H';
	format_str[1] = 'E';
	format_str[2] = 'L';
	format_str[3] = 'L';
	format_str[4] = 'O';
	format_str[5] = ' ';
	format_str[6] = 'W';
	format_str[7] = 'O';
	format_str[8] = 'R';
	format_str[9] = 'L';
	format_str[10] = 'D';
	format_str[11] = '\n';
	format_str[12] = '\0';

	printf(format_str);

}
