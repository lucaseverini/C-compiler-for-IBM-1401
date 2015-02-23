int fact(int n)
{
	return n == 0 ? 1 : n * fact(n - 1);
}

int foo;

int is_prime(int n)
{
	int i;
	for (i = 2; i*i <= n; ++i) if (! n%i) return 0;
	return 1;
}

int main(int argc, char *argv[])
{
	char str[] = "hfjkkjsfd";
	int n = 0;
	printf("%s",str);

	do {
		switch(n % 3)
		{
			case 0:
				printf("%d\n", fact(n));
				break;

			case 1:
				printf("%d\n", is_prime(n));

			default:
				printf("%d, %d\n", fact(n), isPrime(n));
		}
	}
	while (n++ < 10);
}
