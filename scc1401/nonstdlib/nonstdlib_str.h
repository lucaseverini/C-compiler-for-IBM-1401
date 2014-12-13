#ifndef NON_STD_LIB_STR
#define NON_STD_LIB_STR
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
#endif
