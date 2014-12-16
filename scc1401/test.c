#define NON_LIB_PRINTF
#include <nonstdlib.h>

int main() {
    char *tmp = "fail whale\n";
    puts("testing\n");
    // puts(tmp);
    printf("%c\n",'f');
    printf("%s\n",tmp);
}
