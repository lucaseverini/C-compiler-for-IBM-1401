#ifndef NON_STD_LIB_MEM
#define NON_STD_LIB_MEM

// MEM FUNCTIONS
#ifdef NON_LIB_MEM
char* memcpy(char* from, char* to, int size)
{
	while(size --> 0)
		to[size] = from[size];
}
#endif

#endif
