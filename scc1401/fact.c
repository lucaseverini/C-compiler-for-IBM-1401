int f = 1;
int x = 0;

int fact(int n)
{
	if (n)
	{
		f *= n;
		fact(n-1);
	}
}

int main()
{
	int result = 0, i;
	char *prt = 201;
	
	for(i = 10; i ; i--)
	{
		prt[i] = 'A';
	}
	
	result = fact(8);
}

int test(int v)
{
	x = v * 2;
	return v;
}