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
