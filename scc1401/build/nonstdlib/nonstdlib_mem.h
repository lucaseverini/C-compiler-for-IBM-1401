// MEM FUNCTIONS
#ifdef NON_LIB_MEM

char* memcpy(char* from, char* to, int size)
{
	while(size --> 0)
		to[size] = from[size];
}

#endif
