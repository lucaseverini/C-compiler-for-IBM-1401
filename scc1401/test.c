char *test = "testing";

int main() {
    test[0] = 'H';
    asm("MCW @005@,X1","R","D");
}
