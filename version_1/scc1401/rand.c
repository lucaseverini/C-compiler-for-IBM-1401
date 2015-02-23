#include <nonstdlib.h>

int main() {
	int choice = 0;
	int secret = 0;
	secret = nrand(101);
	while (choice != secret)
	{
		printf("%d\n", choice);
		choice = nrand(101);
	}
}
