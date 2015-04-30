int main() 
{
    char* print = (char*)201;
    int i = 232;
    int j = 4;
    int c = 0;
    int r = 1;
    
    c = i+j;
    while(r * 10 < c)
    {
        r *= 10;
    }
    
    while(c)
    {
        char d = '0';
        d = (char)(c / r) + '0';
        *print = d;
        print++;
        c %= r;
        r /= 10;
    }
    
    asm("W");
}
