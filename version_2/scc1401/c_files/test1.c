int main() {
        char *ptr = (int*)201;
        char *alpha = "abcdefghijklmnopqrstuvwxyz";
        int i = 0;
        for (; i < 1; i++)
        {
                *ptr = alpha[i];
                ptr += 1;

        }
}
