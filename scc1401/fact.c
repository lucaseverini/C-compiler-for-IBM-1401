int f = 1;

int fact(int n) {
	if (n) {
		f *= n;
		fact(n-1);
	}
}

int main() {
	fact(6);
}
