int isPrime(int p) {
	int n;
	int m;
	for (n = 2; 2*n <= p; n++) {
		//no modulo, improvising:
		for (m = 2; m*n <= p; m++) {
			if (m*n == p) return 0;
		}
	}
	return 1;
}

int main() {
	int primes[100];
	int primes_found;
	int n = 2;
	
	for (primes_found = 0; primes_found < 100; ++primes_found) {
		while (isPrime(n) == 0) {
			n++;
		}
		primes[primes_found] = n++;
	}
	
}
