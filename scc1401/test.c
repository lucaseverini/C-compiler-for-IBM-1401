char *test = "testing";

int main() {
    char *printLine;
    printLine = "This is a test print line";
    test[0] = 'H';
    asm("MCW @005@,X1","R","D");
}
