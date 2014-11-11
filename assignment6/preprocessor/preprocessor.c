/* preprocessor.c
 * Test file for small-c preprocessor
 * By Luca Severini
 */

// another comment
/*------*/

/*
// nested comment
*/

// Include file
#include "file.h"

// Include system file
#include <file.h>

// Include file
#include file.h

// Define name
#define DEBUG

#define PART_A printf("Hello " 
#define PART_B "World!\n");

#ifdef DEBUG   
	#include "debug_file.h"

	#ifdef XXXXX
		#include xxxxx.h
		#undef XXXXX
	#endif
#else 
	#include "nodebug_file.h"

	#ifdef YYYYY
		#include yyyyy.h
		#undef YYYYY
	#endif
#endif  

#ifndef DEBUG
	#define DEBUG
#endif

#ifndef RELEASE
	#define RELEASE
#endif

#ifdef RELEASE
	#include release.h
	#undef RELEASE
#endif

// Some assembly. Just ignore it
#asm
	MOV A,B
	MOV M,A
#endasm
		
	
#CODE  8000			// Application code begins at the address 8000
#HEAP  3048 4096	// Heap begins at 3048 with a size of 4096 (lowercase "#heap" is interpreted by javacc as a VM statement...)
#DATA  3048 4096	// Same as HEAP above begins at 3048 with a size of 4096
#STACK 1000 2048	// Stack begins at 1000 for 2048 bytes up to 3048
	
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
	
#undef PART_A
#undef PART_B
#undef HEADER
#undef FOOTER
#undef STDIO
#undef STDIO2
	

// End
