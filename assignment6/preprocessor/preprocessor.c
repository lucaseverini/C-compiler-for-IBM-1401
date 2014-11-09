/* preprocessor.c
 * Test file for small-c preprocessor
 * By Luca Severini
 */

// another comment
/*------*/

// Include file
#include "file.h"

// Include system file
#include <file.h>

// Include file
#include file.h

// Define name
#define DEBUG

#define FOO printf("hello"
#define BAR "worlf!\n");

FOO BAR

#ifdef DEBUG
	#include "debug_file.h"
#else
	#include "nodebug_file.h"
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
			
#code  10000
#data  20000
#stack 10000