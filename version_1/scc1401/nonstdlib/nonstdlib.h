#ifndef NON_STD_LIB
#define NON_STD_LIB

//	Usage:
//		Use #define to include the sections of the library that you need then
//		include this library like normal: #include <nonstdlib.h>
//	Example:
//		#define NON_LIB_RAND
//      #define NON_LIB_IO
//		#include <nonstdlib.h>
//
//		The above example only includes the printf and
//		pseudo random number generator

#include <nonstdlib_rand.h>

#include <nonstdlib_mem.h>

#include <string.h>

#include <nonstdlib_io.h>

#include <nonstdlib_str.h>

#endif
