
/* Hello World program */

#define HEADER printf("Example by Luca Severini\n");
#define FOOTER printf("Done.\n");

// Re-define PART_A and PART_B
#define PART_A printf("Hello " 
#define PART_B "World!\n");
#define PART_C oooo

#define STDIO <stdio.h>
#define STDIO2 STDIO
// Include STDIO2
#include STDIO2

#define AA BB
#define BB CC
#define CC printf("CC\n");

int main(int argc, char *argv[])
{
	HEADER
	
    PART_A PART_B
	
	printf("PART_A PART_B PART_C\n");

	AA BB CC
	
	FOOTER
}
