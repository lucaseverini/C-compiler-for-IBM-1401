#ifndef NON_STD_LIB
#define NON_STD_LIB

//	Usage:
//		Use #define to include the sections of the library that you need then
//		include this library like normal: #include <nonstdlib.h>
//	Example:
//		#define NON_LIB_RAND
//		#define NON_LIB_PRINTF
//		#include <nonstdlib.h>
//
//		The above example only includes the printf and
//		pseudo random number generator

#ifdef NON_LIB_STRING
// STRING FUNCTIONS

int to_c_string(char *pstr, char *new_cstr)
{
	char* tmp;
	char size;
	int index = 1;
	size = *pstr;
	tmp = new_cstr;
	while(index++ < size)
	{
		*new_cstr++ = pstr[index];
	}
	new_cstr[index] = 0;
	new_cstr = tmp;
	return 0;
}

int to_p_string(char* cstr, char* new_pstr)
{
	char c;
	char size;
	int index = 1;
	while(c = cstr[(index++)-1], c != 0)
	{
		new_pstr[index] = c;
		size += 1;
	}
	new_pstr[0] = size;
	return 0;
}
#endif

// MEM FUNCTIONS
#ifdef NON_LIB_MEM
char* memcpy(char* from, char* to, int size)
{
	while(size --> 0)
		to[size] = from[size];
}
#endif
// PRINTF
#ifdef NON_LIB_PRINTF

#ifdef C_STRINGS
	#define printf cprintf
#endif

#ifdef P_STRINGS
	#define printf pprintf
#endif

int pprintf(char *pformat_str, ...)
{
	char *arg;
	int size_of_str;
	int index = 0;
	char *delta = (char *)15995;
	arg = pformat_str + delta;
	size_of_str = *pformat_str;
	while(index < size_of_str)
	{
		if (pformat_str[index] == '%')
		{
			if (pformat_str[index+1] == '%'){ putc('%'); index++; }
				else if(*(cformat_str+1) == 'd'){ putnum(*arg); arg += delta; index++; }
				else if (*(cformat_str+1) == 'c'){ putc(*arg); arg += delta; index++; }
		} else {
			putc(pformat_str[index]);
		}
		index ++;
	}
}

int cprintf(char *cformat_str, ...)
{
	char *arg;
	// delta is the int value of the char ptr to -1
	// this allows us to speed up the pointer maths as
	// addition is faster than subtraction
	char *delta = (char *)15995;
	arg = cformat_str + delta;

	while(*cformat_str != 0)
	{
		if (*cformat_str == '%')
		{
			if (*(cformat_str+1) == '%'){ putc('%'); cformat_str++; }
				else if(*(cformat_str+1) == 'd'){ prtis((int)(*arg)); arg += delta; cformat_str++; }
				else if (*(cformat_str+1) == 'c'){ putc(*arg); arg += delta; cformat_str++; }
		} else {
			putc(*cformat_str);
		}
		cformat_str ++;
	}
}
#endif

// RANDOM NUMBERS
#ifdef NON_LIB_RAND

// Set the seed to a inital value
// Also use the 1st char of the 1st card if availible
int seed = 6574;

// Generate a random number
int genRand()
{
	seed = ((42 * seed) + 19) % 100000;
	return seed;
}

// Generates a random number between [0, end)
int nrand(int end)
{
	return genRand() % end;
}
#endif

#endif
