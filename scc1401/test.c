#define NON_LIB_PRINTF
#include <nonstdlib.h>

int main() {
    char *tmp = "fail whale\n";
    printf("%c\n",'f');
    printf("testing\n");
    printf("%s\n",tmp);
}
