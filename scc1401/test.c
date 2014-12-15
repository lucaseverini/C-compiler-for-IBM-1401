char *print_area = (char *)201;
char *hello = "Hello World";

int main() {
    int i = 0;
    for (i = 0; i < 11; ++i) {
        print_area[i] = hello[i];
    }
    asm("W");
}
