#include <stdio.h>

int seed = 6574;

int genRand()
{
    seed = ((42 * seed) + 19) % 100000;
    return seed;
}

int nrand(int end)
{
    return genRand() % end;
}

int main(int argc, const char * argv[]) {
    int i = 1;
    for (; i < 61; i ++) {
        printf("Full random %d\n",genRand());
        printf("Range random %d\n",nrand(i));
    }
    
    return 0;
}
