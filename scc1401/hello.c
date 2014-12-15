char* print_area = (char*)201;
char* hw = "hello world";

int main() {
	int i = 0;
	for(i = 0; i < 11; i ++)
	{
		print_area[i] = hw[i];
	}
	asm("W");
}
