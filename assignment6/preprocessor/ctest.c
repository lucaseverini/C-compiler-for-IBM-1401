
/* Hello World program */

#define HEADER printf("Example by Luca Severini\n");
#define FOOTER printf("Done.\n");

#define PART_A printf("Hello "
#define PART_B "World!\n");

#define STDIO <stdio.h>
#define STDIO2 STDIO
#include STDIO2

int main(int argc, char *argv[])
{
	HEADER
	
    PART_A PART_B
	
	FOOTER
}