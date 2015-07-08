****************************************************************     READ      EQU  001               * Read area
     PUNCH     EQU  101               * Punch area
     PRINT     EQU  201               * Print area
     PRCPOS    DCW  000               * char position in print area
     PUCPOS    DCW  000               * char position in punch area
     PUNSIZ    DCW  @080@             * Size of punch area
     PRTSIZ    DCW  @132@             * Size of print area
     EOS       DCW  @'@               * End Of String char
     EOL       DCW  @;@               * End Of Line char
               ORG  87
     X1        DSA  0                 * Index Register 1
               ORG  92
     X2        DSA  0                 * Index Register 2 (stack pointer)
               ORG  97
     X3        DSA  0                 * Index Register 3 (stack frame pointer)
****************************************************************     * GLOBAL/STATIC DATA AND VARIABLES
     * seed size:5 offset:1000
     * CONST_STR_LITERAL_1 size:17 offset:1005
     * __putchar_pos size:3 offset:1022
     * __putchar_last size:3 offset:1025
     * __getCharPosition size:3 offset:1028
     * CONST_STR_LITERAL_8 size:10 offset:1031
     * CONST_STR_LITERAL_9 size:6 offset:1041
     * CONST_STR_LITERAL_10 size:6 offset:1047
     * CONST_STR_LITERAL_11 size:4 offset:1053
               ORG  1000              * seed
               DCW  @69105@
               ORG  1005              * CONST_STR_LITERAL_1
               DCW  @0@
               DCW  @1@
               DCW  @2@
               DCW  @3@
               DCW  @4@
               DCW  @5@
               DCW  @6@
               DCW  @7@
               DCW  @8@
               DCW  @9@
               DCW  @A@
               DCW  @B@
               DCW  @C@
               DCW  @D@
               DCW  @E@
               DCW  @F@
               DCW  @'@
               ORG  1022              * __putchar_pos
               DCW  @201@
               ORG  1025              * __putchar_last
               DCW  @200@
               ORG  1028              * __getCharPosition
               DCW  @081@
               ORG  1031              * CONST_STR_LITERAL_8
               DCW  @F@
               DCW  @I@
               DCW  @Z@
               DCW  @Z@
               DCW  @B@
               DCW  @U@
               DCW  @Z@
               DCW  @Z@
               DCW  @;@
               DCW  @'@
               ORG  1041              * CONST_STR_LITERAL_9
               DCW  @F@
               DCW  @I@
               DCW  @Z@
               DCW  @Z@
               DCW  @;@
               DCW  @'@
               ORG  1047              * CONST_STR_LITERAL_10
               DCW  @B@
               DCW  @U@
               DCW  @Z@
               DCW  @Z@
               DCW  @;@
               DCW  @'@
               ORG  1053              * CONST_STR_LITERAL_11
               DCW  @%@
               DCW  @D@
               DCW  @;@
               DCW  @'@
     * START POSITION OF PROGRAM CODE
               ORG  1057
     * SET X2 TO BE THE STACK POINTER (STACK GROWS UPWARD)
     START     SBR  X2,399            * Set X2 to stack pointer value
               MCW  X2,X3             * Copy stack pointer in X3
               B    LDFAAA            * Jump to function main
               H                      * Program executed. System halts
     ********************************************************************************
     * Function : genRand
     ********************************************************************************
     LBAAAA    SBR  3+X3              * Save return address in register B in stack frame (X3)
     * Set the right WM and clear the wrong ones
               SW   1+X3              * Set WM at 1+X3
               CW   2+X3              * Clear WM at 2+X3
               CW   3+X3              * Clear WM at 3+X3
     ***************************************
     * Begin [Block ending at LCAAAA]
     * Push (3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
     * Modulo (%) (((42 * seed) + 19) % 100000)
     * Constant (100000 : @100000@)
     * Push (@100000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LNHAAA,0+X2       * Load data @100000@ in stack
     * Addition ((42 * seed) + 19)
     * Multiply (42 * seed)
     * Constant (42 : @00042@)
     * Push (@00042@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LOHAAA,0+X2       * Load data @00042@ in stack
     * Static Variable (seed : 1004)
     * Push (1004:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  1004,0+X2         * Load memory 1004 in stack
               M    15995+X2,6+X2     * Multiply stack at -5 to stack at 6
               SW   2+X2              * Set WM in stack at 2
               LCA  6+X2,15995+X2     * Load stack at 6 to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Constant (19 : @00019@)
     * Push (@00019@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LQHAAA,0+X2       * Load data @00019@ in stack
               A    0+X2,15995+X2     * Add stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               B    SNPDIV            * Jump to snippet SNIP_DIV
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@'04@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LRHAAA,0+X2       * Load data @'04@ in stack
     * Assignment (seed = (((42 * seed) + 19) % 100000))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:5)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Assignment (seed = (((42 * seed) + 19) % 100000))
     * Put on stack return value (seed)
     * Static Variable (seed : 1004)
     * Push (1004:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  1004,0+X2         * Load memory 1004 in stack
     * Pop (15997+X3:5)
               LCA  0+X2,15997+X3     * Load stack in 15997+X3
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (3)
     LCAAAA    MA   LSHAAA,X2         * Add -3 to stack pointer
     * End [Block ending at LCAAAA]
     ***************************************
               LCA  3+X3,X1           * Load return address in X1
               B    0+X1              * Jump back to caller in X1
     ********************************************************************************
     * End Function : genRand
     ********************************************************************************
     ********************************************************************************
     * Function : itoa
     ********************************************************************************
     LYAAAA    SBR  3+X3              * Save return address in register B in stack frame (X3)
     * Set the right WM and clear the wrong ones
               SW   1+X3              * Set WM at 1+X3
               CW   2+X3              * Clear WM at 2+X3
               CW   3+X3              * Clear WM at 3+X3
     ***************************************
     * Begin [Block ending at LZAAAA]
     * start size:3 offset:3
     * digits size:3 offset:6
     * exp size:5 offset:9
               LCA  LTHAAA,9+X3       * Load *char 1005 into memory 9+X3
               LCA  LUHAAA,14+X3      * Load int 1 into memory 14+X3
     * Push (14)
               MA   LVHAAA,X2         * Add 14 to stack pointer
     * Parameter Variable (str : 15992+X3)
     * Push (15992+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  15992+X3,0+X2     * Load memory 15992+X3 in stack
     * Push (@006@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LWHAAA,0+X2       * Load data @006@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (start = str)
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (start = str)
     * If [if ((value < 0) then [Block ending at LABAAA] else [if ((value == 0) then [Block ending at LBBAAA]]]
     * Less (value < 0)
     * Parameter Variable (value : 15997+X3)
     * Push (15997+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
               B    CLNNMN            * Jump to snippet clean_number
     * Constant (0 : @00000@)
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               MCW  LXHAAA,0+X2       * Move 0 in stack
               BL   LTFAAA            * Jump if less
               B    LUFAAA            * Jump to End
     LTFAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LUFAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LDBAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LABAAA]
     * Constant ('-' : @-@)
     * Push (@-@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LZHAAA,0+X2       * Load data @-@ in stack
     * PostIncrement (str++)
     * Push (@I9B@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LAIAAA,0+X2       * Load data @I9B@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LYHAAA,0+X1       * Postincrement pointer at X1
     * Assignment ((*(str++)) = '-')
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment ((*(str++)) = '-')
     * Negate (-value)
     * Parameter Variable (value : 15997+X3)
     * Push (15997+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
               ZS   0+X2
               B    CLNNMN            * Jump to snippet clean_number
     * Push (@I9G@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (value = (-value))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:5)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Assignment (value = (-value))
     * End [Block ending at LABAAA]
     ***************************************
     LABAAA    B    LGBAAA            * Jump when true
     * If [if ((value == 0) then [Block ending at LBBAAA]]
     * Equal (value == 0)
     * Parameter Variable (value : 15997+X3)
     * Push (15997+X3:5)
     LDBAAA    MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
               B    CLNNMN            * Jump to snippet clean_number
     * Constant (0 : @00000@)
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LVFAAA            * Jump if equal
               B    LWFAAA            * Jump to End
     LVFAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LWFAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LGBAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LBBAAA]
     * Constant ('0' : @0@)
     * Push (@0@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LCIAAA,0+X2       * Load data @0@ in stack
     * SubScript (str[0])
     * Parameter Variable (str : 15992+X3)
     * Push (15992+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  15992+X3,0+X2     * Load memory 15992+X3 in stack
     * End SubScript (str[0])
     * Assignment ((str[0]) = '0')
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment ((str[0]) = '0')
     * Constant ('\0' : EOS)
     * Push (EOS:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  EOS,0+X2          * Load memory EOS in stack
     * SubScript (str[1])
     * Parameter Variable (str : 15992+X3)
     * Push (15992+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  15992+X3,0+X2     * Load memory 15992+X3 in stack
               A    LUHAAA,0+X2       * Add offset 1 to point element 1
     * End SubScript (str[1])
     * Assignment ((str[1]) = '\0')
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment ((str[1]) = '\0')
     * Return to LZAAAA with return value start
     * Put on stack return value (start)
     * Local Variable (start : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Pop (15984+X3:3)
               LCA  0+X2,15984+X3     * Load stack in 15984+X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
               B    LZAAAA            * Jump to end of function block
     * End [Block ending at LBBAAA]
     ***************************************
     * End If [if ((value == 0) then [Block ending at LBBAAA]]
     * End If [if ((value < 0) then [Block ending at LABAAA] else [if ((value == 0) then [Block ending at LBBAAA]]]
     * While [while ((exp <= (value / base))) [Block ending at LFBAAA] top:LGBAAA bottom:LHBAAA]
     * LessOrEqual (exp <= (value / base))
     * Local Variable (exp : 14+X3)
     * Push (14+X3:5)
     LGBAAA    MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  14+X3,0+X2        * Load memory 14+X3 in stack
               B    CLNNMN            * Jump to snippet clean_number
     * Divide (value / base)
     * Parameter Variable (base : 15989+X3)
     * Push (15989+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15989+X3,0+X2     * Load memory 15989+X3 in stack
     * Parameter Variable (value : 15997+X3)
     * Push (15997+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
               B    SNPDIV            * Jump to snippet SNIP_DIV
               MCW  0+X2,15995+X2     * Move stack in stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               MCW  LUHAAA,0+X2       * Move 1 in stack
               BH   LXFAAA            * Jump if less or equal
               B    LYFAAA            * Jump to End
     LXFAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LYFAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LJBAAA,5+X2,      * Jump to bottom of While
     ***************************************
     * Begin [Block ending at LFBAAA]
     * Multiply (exp * base)
     * Local Variable (exp : 14+X3)
     * Push (14+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  14+X3,0+X2        * Load memory 14+X3 in stack
     * Parameter Variable (base : 15989+X3)
     * Push (15989+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15989+X3,0+X2     * Load memory 15989+X3 in stack
               M    15995+X2,6+X2     * Multiply stack at -5 to stack at 6
               SW   2+X2              * Set WM in stack at 2
               LCA  6+X2,15995+X2     * Load stack at 6 to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@014@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LVHAAA,0+X2       * Load data @014@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (exp = (exp * base))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:5)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Assignment (exp = (exp * base))
     * End [Block ending at LFBAAA]
     ***************************************
     LFBAAA    B    LGBAAA            * Jump to top of While
     * End While [while ((exp <= (value / base))) [Block ending at LFBAAA] top:LGBAAA bottom:LHBAAA]
     * While [while (exp) [Block ending at LIBAAA] top:LJBAAA bottom:LKBAAA]
     * Local Variable (exp : 14+X3)
     * Push (14+X3:5)
     LJBAAA    MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  14+X3,0+X2        * Load memory 14+X3 in stack
               MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LKBAAA,5+X2,      * Jump to bottom of While
     ***************************************
     * Begin [Block ending at LIBAAA]
     * SubScript (digits[(value / exp)])
     * Local Variable (digits : 9+X3)
     * Push (9+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  9+X3,0+X2         * Load memory 9+X3 in stack
     * Divide (value / exp)
     * Local Variable (exp : 14+X3)
     * Push (14+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  14+X3,0+X2        * Load memory 14+X3 in stack
     * Parameter Variable (value : 15997+X3)
     * Push (15997+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
               B    SNPDIV            * Jump to snippet SNIP_DIV
               MCW  0+X2,15995+X2     * Move stack in stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Put raw index on the stack
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
               M    15995+X2,6+X2     * Multiply stack at -5 to stack at 6
               SW   2+X2              * Set WM in stack at 2
               LCA  6+X2,15995+X2     * Load stack at 6 in stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Stack top is now array index
               B    NMNPTR            * Jump to snippet number_to_pointer
               MA   0+X2,15997+X2     * Add stack to stack at -3
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Stack top is location in array now
     * End SubScript (digits[(value / exp)])
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * PostIncrement (str++)
     * Push (@I9B@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LAIAAA,0+X2       * Load data @I9B@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LYHAAA,0+X1       * Postincrement pointer at X1
     * Assignment ((*(str++)) = (digits[(value / exp)]))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment ((*(str++)) = (digits[(value / exp)]))
     * Modulo (%) (value % exp)
     * Local Variable (exp : 14+X3)
     * Push (14+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  14+X3,0+X2        * Load memory 14+X3 in stack
     * Parameter Variable (value : 15997+X3)
     * Push (15997+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
               B    SNPDIV            * Jump to snippet SNIP_DIV
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@I9G@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (value = (value % exp))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:5)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Assignment (value = (value % exp))
     * Divide (exp / base)
     * Parameter Variable (base : 15989+X3)
     * Push (15989+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  15989+X3,0+X2     * Load memory 15989+X3 in stack
     * Local Variable (exp : 14+X3)
     * Push (14+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  14+X3,0+X2        * Load memory 14+X3 in stack
               B    SNPDIV            * Jump to snippet SNIP_DIV
               MCW  0+X2,15995+X2     * Move stack in stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@014@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LVHAAA,0+X2       * Load data @014@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (exp = (exp / base))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:5)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Assignment (exp = (exp / base))
     * End [Block ending at LIBAAA]
     ***************************************
     LIBAAA    B    LJBAAA            * Jump to top of While
     * End While [while (exp) [Block ending at LIBAAA] top:LJBAAA bottom:LKBAAA]
     * Constant ('\0' : EOS)
     * Push (EOS:1)
     LKBAAA    MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  EOS,0+X2          * Load memory EOS in stack
     * Parameter Variable (str : 15992+X3)
     * Push (15992+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  15992+X3,0+X2     * Load memory 15992+X3 in stack
     * Assignment ((*str) = '\0')
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment ((*str) = '\0')
     * Put on stack return value (start)
     * Local Variable (start : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Pop (15984+X3:3)
               LCA  0+X2,15984+X3     * Load stack in 15984+X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (14)
     LZAAAA    MA   LDIAAA,X2         * Add -14 to stack pointer
     * End [Block ending at LZAAAA]
     ***************************************
               LCA  3+X3,X1           * Load return address in X1
               B    0+X1              * Jump back to caller in X1
     ********************************************************************************
     * End Function : itoa
     ********************************************************************************
     ********************************************************************************
     * Function : main
     ********************************************************************************
     LDFAAA    SBR  3+X3              * Save return address in register B in stack frame (X3)
     * Set the right WM and clear the wrong ones
               SW   1+X3              * Set WM at 1+X3
               CW   2+X3              * Clear WM at 2+X3
               CW   3+X3              * Clear WM at 3+X3
     ***************************************
     * Begin [Block ending at LEFAAA]
     * i size:5 offset:3
     * Push (8)
               MA   LEIAAA,X2         * Add 8 to stack pointer
     * For [for ((i = 1); (i <= 100); (++i)) [Block ending at LFFAAA] top:LQFAAA bottom:LRFAAA continue:LSFAAA]
     * Constant (1 : @00001@)
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
     * Push (@008@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LEIAAA,0+X2       * Load data @008@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (i = 1)
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:5)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Assignment (i = 1)
     * LessOrEqual (i <= 100)
     * Local Variable (i : 8+X3)
     * Push (8+X3:5)
     LQFAAA    MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  8+X3,0+X2         * Load memory 8+X3 in stack
               B    CLNNMN            * Jump to snippet clean_number
     * Constant (100 : @00100@)
     * Push (@00100@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LFIAAA,0+X2       * Load data @00100@ in stack
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               MCW  LUHAAA,0+X2       * Move 1 in stack
               BH   LZFAAA            * Jump if less or equal
               B    LAGAAA            * Jump to End
     LZFAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LAGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LEFAAA,5+X2,      * Jump to bottom of For
     ***************************************
     * Begin [Block ending at LFFAAA]
     * If [if ((((i % 3) == 0) && ((i % 5) == 0)) then [Block ending at LGFAAA] else [if (((i % 3) == 0) then [Block ending at LHFAAA] else [if (((i % 5) == 0) then [Block ending at LIFAAA] else [Block ending at LJFAAA]]]]
     * And (((i % 3) == 0) && ((i % 5) == 0))
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
     * Equal ((i % 3) == 0)
     * Modulo (%) (i % 3)
     * Constant (3 : @00003@)
     * Push (@00003@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LGIAAA,0+X2       * Load data @00003@ in stack
     * Local Variable (i : 8+X3)
     * Push (8+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  8+X3,0+X2         * Load memory 8+X3 in stack
               B    SNPDIV            * Jump to snippet SNIP_DIV
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               B    CLNNMN            * Jump to snippet clean_number
     * Constant (0 : @00000@)
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LDGAAA            * Jump if equal
               B    LEGAAA            * Jump to End
     LDGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LEGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LBGAAA,5+X2,      * Jump to Zero if equal
     * Equal ((i % 5) == 0)
     * Modulo (%) (i % 5)
     * Constant (5 : @00005@)
     * Push (@00005@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LHIAAA,0+X2       * Load data @00005@ in stack
     * Local Variable (i : 8+X3)
     * Push (8+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  8+X3,0+X2         * Load memory 8+X3 in stack
               B    SNPDIV            * Jump to snippet SNIP_DIV
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               B    CLNNMN            * Jump to snippet clean_number
     * Constant (0 : @00000@)
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LFGAAA            * Jump if equal
               B    LGGAAA            * Jump to End
     LFGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LGGAAA    MCS  0+X2,0+X2         * Clear WM
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LBGAAA,5+X2,      * Jump to Zero if equal
               B    LCGAAA            * Jump to End
     LBGAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LCGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LOFAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LGFAAA]
     * Function Call printf(CONST_STR_LITERAL_8)
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * Static Array (CONST_STR_LITERAL_8:char [10])
     * Push (@'31@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LIIAAA,0+X2       * Load data @'31@ in stack
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LAEAAA            * Jump to function printf
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call printf(CONST_STR_LITERAL_8)
     * End [Block ending at LGFAAA]
     ***************************************
     LGFAAA    B    LSFAAA            * Jump when true
     * If [if (((i % 3) == 0) then [Block ending at LHFAAA] else [if (((i % 5) == 0) then [Block ending at LIFAAA] else [Block ending at LJFAAA]]]
     * Equal ((i % 3) == 0)
     * Modulo (%) (i % 3)
     * Constant (3 : @00003@)
     * Push (@00003@:5)
     LOFAAA    MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LGIAAA,0+X2       * Load data @00003@ in stack
     * Local Variable (i : 8+X3)
     * Push (8+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  8+X3,0+X2         * Load memory 8+X3 in stack
               B    SNPDIV            * Jump to snippet SNIP_DIV
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               B    CLNNMN            * Jump to snippet clean_number
     * Constant (0 : @00000@)
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LHGAAA            * Jump if equal
               B    LIGAAA            * Jump to End
     LHGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LIGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LMFAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LHFAAA]
     * Function Call printf(CONST_STR_LITERAL_9)
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * Static Array (CONST_STR_LITERAL_9:char [6])
     * Push (@'41@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LJIAAA,0+X2       * Load data @'41@ in stack
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LAEAAA            * Jump to function printf
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call printf(CONST_STR_LITERAL_9)
     * End [Block ending at LHFAAA]
     ***************************************
     LHFAAA    B    LSFAAA            * Jump when true
     * If [if (((i % 5) == 0) then [Block ending at LIFAAA] else [Block ending at LJFAAA]]
     * Equal ((i % 5) == 0)
     * Modulo (%) (i % 5)
     * Constant (5 : @00005@)
     * Push (@00005@:5)
     LMFAAA    MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LHIAAA,0+X2       * Load data @00005@ in stack
     * Local Variable (i : 8+X3)
     * Push (8+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  8+X3,0+X2         * Load memory 8+X3 in stack
               B    SNPDIV            * Jump to snippet SNIP_DIV
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               B    CLNNMN            * Jump to snippet clean_number
     * Constant (0 : @00000@)
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LJGAAA            * Jump if equal
               B    LKGAAA            * Jump to End
     LJGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LKGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LKFAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LIFAAA]
     * Function Call printf(CONST_STR_LITERAL_10)
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * Static Array (CONST_STR_LITERAL_10:char [6])
     * Push (@'47@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LKIAAA,0+X2       * Load data @'47@ in stack
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LAEAAA            * Jump to function printf
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call printf(CONST_STR_LITERAL_10)
     * End [Block ending at LIFAAA]
     ***************************************
     LIFAAA    B    LSFAAA            * Jump when true
     ***************************************
     * Begin [Block ending at LJFAAA]
     * Function Call printf(CONST_STR_LITERAL_11, i)
     * Push (5)
     LKFAAA    MA   LMHAAA,X2         * Add 5 to stack pointer
     * Local Variable (i : 8+X3)
     * Push (8+X3:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  8+X3,0+X2         * Load memory 8+X3 in stack
     * Static Array (CONST_STR_LITERAL_11:char [4])
     * Push (@'53@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LLIAAA,0+X2       * Load data @'53@ in stack
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LAEAAA            * Jump to function printf
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call printf(CONST_STR_LITERAL_11, i)
     * End [Block ending at LJFAAA]
     ***************************************
     * End If [if (((i % 5) == 0) then [Block ending at LIFAAA] else [Block ending at LJFAAA]]
     * End If [if (((i % 3) == 0) then [Block ending at LHFAAA] else [if (((i % 5) == 0) then [Block ending at LIFAAA] else [Block ending at LJFAAA]]]
     * End If [if ((((i % 3) == 0) && ((i % 5) == 0)) then [Block ending at LGFAAA] else [if (((i % 3) == 0) then [Block ending at LHFAAA] else [if (((i % 5) == 0) then [Block ending at LIFAAA] else [Block ending at LJFAAA]]]]
     * End [Block ending at LFFAAA]
     ***************************************
     * PreIncrement((++i)
     * Push (@008@:3)
     LSFAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LEIAAA,0+X2       * Load data @008@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
               A    LUHAAA,0+X1       * Preincrement memory at X1
               B    LQFAAA            * Jump to top of For
     * End For [for ((i = 1); (i <= 100); (++i)) [Block ending at LFFAAA] top:LQFAAA bottom:LRFAAA continue:LSFAAA]
     * Pop (8)
     LEFAAA    MA   LAIAAA,X2         * Add -8 to stack pointer
     * End [Block ending at LEFAAA]
     ***************************************
               LCA  3+X3,X1           * Load return address in X1
               B    0+X1              * Jump back to caller in X1
     ********************************************************************************
     * End Function : main
     ********************************************************************************
     ********************************************************************************
     * Function : printf
     ********************************************************************************
     LAEAAA    SBR  3+X3              * Save return address in register B in stack frame (X3)
     * Set the right WM and clear the wrong ones
               SW   1+X3              * Set WM at 1+X3
               CW   2+X3              * Clear WM at 2+X3
               CW   3+X3              * Clear WM at 3+X3
     ***************************************
     * Begin [Block ending at LBEAAA]
     * arg size:3 offset:3
     * c size:1 offset:6
     * addrP size:3 offset:7
     * addrC size:1 offset:10
     * Push (11)
               MA   LMIAAA,X2         * Add 11 to stack pointer
     * Addition (( &cformat_str ) + 15997)
     * AddressOf ( &cformat_str )
     * Push (@I9G@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Constant (15997 : @I9G@)
     * Push (@I9G@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   0+X2,15997+X2     * Add stack to stack at -3
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (@006@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LWHAAA,0+X2       * Load data @006@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (arg = ((*char) (( &cformat_str ) + 15997)))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (arg = ((*char) (( &cformat_str ) + 15997)))
     * While [while (((c = (*(cformat_str++))) != '\0')) [Block ending at LCEAAA] top:LXEAAA bottom:LYEAAA]
     * NotEqual (!=) ((c = (*(cformat_str++))) != '\0')
     * DereferenceExpression (*(cformat_str++))
     * PostIncrement (cformat_str++)
     * Push (@I9G@:3)
     LXEAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LYHAAA,0+X1       * Postincrement pointer at X1
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*(cformat_str++))
     * Push (@007@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LNIAAA,0+X2       * Load data @007@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (c = (*(cformat_str++)))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
               LCA  0+X2,0+X1         * Load stack in memory X1
     * End Assignment (c = (*(cformat_str++)))
     * Constant ('\0' : EOS)
     * Push (EOS:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  EOS,0+X2          * Load memory EOS in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
               BE   LLGAAA            * Jump if equal
               B    LMGAAA            * Jump to End
     LLGAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LMGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LBEAAA,5+X2,      * Jump to bottom of While
     ***************************************
     * Begin [Block ending at LCEAAA]
     * If [if ((c != '%') then [Block ending at LDEAAA] else [Block ending at LEEAAA]]
     * NotEqual (!=) (c != '%')
     * Local Variable (c : 7+X3)
     * Push (7+X3:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  7+X3,0+X2         * Load memory 7+X3 in stack
     * Constant ('%' : @%@)
     * Push (@%@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LOIAAA,0+X2       * Load data @%@ in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
               BE   LNGAAA            * Jump if equal
               B    LOGAAA            * Jump to End
     LNGAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LOGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LVEAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LDEAAA]
     * Function Call putchar(c)
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * Local Variable (c : 7+X3)
     * Push (7+X3:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  7+X3,0+X2         * Load memory 7+X3 in stack
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LWCAAA            * Jump to function putchar
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call putchar(c)
     * End [Block ending at LDEAAA]
     ***************************************
     LDEAAA    B    LCEAAA            * Jump when true
     ***************************************
     * Begin [Block ending at LEEAAA]
     * DereferenceExpression (*(cformat_str++))
     * PostIncrement (cformat_str++)
     * Push (@I9G@:3)
     LVEAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LYHAAA,0+X1       * Postincrement pointer at X1
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*(cformat_str++))
     * Push (@007@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LNIAAA,0+X2       * Load data @007@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (c = (*(cformat_str++)))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment (c = (*(cformat_str++)))
     * If [if ((c == '%') then [Block ending at LFEAAA] else [if ((c == 'C') then [Block ending at LGEAAA] else [if ((c == 'S') then [Block ending at LHEAAA] else [if ((c == 'D') then [Block ending at LIEAAA] else [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]]]]]
     * Equal (c == '%')
     * Local Variable (c : 7+X3)
     * Push (7+X3:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  7+X3,0+X2         * Load memory 7+X3 in stack
     * Constant ('%' : @%@)
     * Push (@%@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LOIAAA,0+X2       * Load data @%@ in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LPGAAA            * Jump if equal
               B    LQGAAA            * Jump to End
     LPGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LQGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LTEAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LFEAAA]
     * Function Call putchar('%')
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * Constant ('%' : @%@)
     * Push (@%@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LOIAAA,0+X2       * Load data @%@ in stack
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LWCAAA            * Jump to function putchar
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call putchar('%')
     * End [Block ending at LFEAAA]
     ***************************************
     LFEAAA    B    LCEAAA            * Jump when true
     * If [if ((c == 'C') then [Block ending at LGEAAA] else [if ((c == 'S') then [Block ending at LHEAAA] else [if ((c == 'D') then [Block ending at LIEAAA] else [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]]]]
     * Equal (c == 'C')
     * Local Variable (c : 7+X3)
     * Push (7+X3:1)
     LTEAAA    MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  7+X3,0+X2         * Load memory 7+X3 in stack
     * Constant ('C' : @C@)
     * Push (@C@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LPIAAA,0+X2       * Load data @C@ in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LRGAAA            * Jump if equal
               B    LSGAAA            * Jump to End
     LRGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LSGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LREAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LGEAAA]
     * Function Call putchar((*arg))
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * DereferenceExpression (*arg)
     * Local Variable (arg : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*arg)
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LWCAAA            * Jump to function putchar
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call putchar((*arg))
     * Addition (arg + 15999)
     * Local Variable (arg : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Constant (15999 : @I9I@)
     * Push (@I9I@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LBIAAA,0+X2       * Load data @I9I@ in stack
               MA   0+X2,15997+X2     * Add stack to stack at -3
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (@006@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LWHAAA,0+X2       * Load data @006@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (arg = (arg + 15999))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (arg = (arg + 15999))
     * End [Block ending at LGEAAA]
     ***************************************
     LGEAAA    B    LCEAAA            * Jump when true
     * If [if ((c == 'S') then [Block ending at LHEAAA] else [if ((c == 'D') then [Block ending at LIEAAA] else [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]]]
     * Equal (c == 'S')
     * Local Variable (c : 7+X3)
     * Push (7+X3:1)
     LREAAA    MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  7+X3,0+X2         * Load memory 7+X3 in stack
     * Constant ('S' : @S@)
     * Push (@S@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LQIAAA,0+X2       * Load data @S@ in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LTGAAA            * Jump if equal
               B    LUGAAA            * Jump to End
     LTGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LUGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LPEAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LHEAAA]
     * Function Call puts((*((**char) arg)))
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * DereferenceExpression (*((**char) arg))
     * Local Variable (arg : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*((**char) arg))
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LHDAAA            * Jump to function puts
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call puts((*((**char) arg)))
     * Addition (arg + 15997)
     * Local Variable (arg : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Constant (15997 : @I9G@)
     * Push (@I9G@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   0+X2,15997+X2     * Add stack to stack at -3
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (@006@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LWHAAA,0+X2       * Load data @006@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (arg = (arg + 15997))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (arg = (arg + 15997))
     * End [Block ending at LHEAAA]
     ***************************************
     LHEAAA    B    LCEAAA            * Jump when true
     * If [if ((c == 'D') then [Block ending at LIEAAA] else [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]]
     * Equal (c == 'D')
     * Local Variable (c : 7+X3)
     * Push (7+X3:1)
     LPEAAA    MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  7+X3,0+X2         * Load memory 7+X3 in stack
     * Constant ('D' : @D@)
     * Push (@D@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LRIAAA,0+X2       * Load data @D@ in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LVGAAA            * Jump if equal
               B    LWGAAA            * Jump to End
     LVGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LWGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LNEAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LIEAAA]
     * a size:7 offset:11
     * Push (7)
               MA   LNIAAA,X2         * Add 7 to stack pointer
     * Function Call itoa((*((*int) arg)), a, 10)
     * Push (3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
     * Constant (10 : @00010@)
     * Push (@00010@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LSIAAA,0+X2       * Load data @00010@ in stack
     * Local Array (a:char [7])
     * Push (@012@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LTIAAA,0+X2       * Load data @012@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * DereferenceExpression (*((*int) arg))
     * Local Variable (arg : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*((*int) arg))
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LYAAAA            * Jump to function itoa
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Function Call itoa((*((*int) arg)), a, 10)
     * Function Call puts(a)
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * Local Array (a:char [7])
     * Push (@012@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LTIAAA,0+X2       * Load data @012@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LHDAAA            * Jump to function puts
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call puts(a)
     * Addition (arg + 15995)
     * Local Variable (arg : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Constant (15995 : @I9E@)
     * Push (@I9E@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LPHAAA,0+X2       * Load data @I9E@ in stack
               MA   0+X2,15997+X2     * Add stack to stack at -3
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (@006@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LWHAAA,0+X2       * Load data @006@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (arg = (arg + 15995))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (arg = (arg + 15995))
     * Pop (7)
     LIEAAA    MA   LUIAAA,X2         * Add -7 to stack pointer
     * End [Block ending at LIEAAA]
     ***************************************
               B    LCEAAA            * Jump when true
     * If [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]
     * Equal (c == 'P')
     * Local Variable (c : 7+X3)
     * Push (7+X3:1)
     LNEAAA    MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  7+X3,0+X2         * Load memory 7+X3 in stack
     * Constant ('P' : @P@)
     * Push (@P@:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LVIAAA,0+X2       * Load data @P@ in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LXGAAA            * Jump if equal
               B    LYGAAA            * Jump to End
     LXGAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LYGAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LLEAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LJEAAA]
     * Local Variable (arg : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Push (@010@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LWIAAA,0+X2       * Load data @010@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (addrP = arg)
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (addrP = arg)
     * DereferenceExpression (*addrP)
     * Local Variable (addrP : 10+X3)
     * Push (10+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  10+X3,0+X2        * Load memory 10+X3 in stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*addrP)
     * Push (@011@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LMIAAA,0+X2       * Load data @011@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (addrC = (*addrP))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment (addrC = (*addrP))
     * Function Call putchar(addrC)
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * Local Variable (addrC : 11+X3)
     * Push (11+X3:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  11+X3,0+X2        * Load memory 11+X3 in stack
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LWCAAA            * Jump to function putchar
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call putchar(addrC)
     * Start asm block
               W                      * Start asm block
     * End asm block
     * Start asm block
               W                      * Start asm block
     * End asm block
     * Start asm block
               W                      * Start asm block
     * End asm block
     * Addition (arg + 15999)
     * Local Variable (arg : 6+X3)
     * Push (6+X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  6+X3,0+X2         * Load memory 6+X3 in stack
     * Constant (15999 : @I9I@)
     * Push (@I9I@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LBIAAA,0+X2       * Load data @I9I@ in stack
               MA   0+X2,15997+X2     * Add stack to stack at -3
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (@006@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LWHAAA,0+X2       * Load data @006@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Assignment (arg = (arg + 15999))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (arg = (arg + 15999))
     * End [Block ending at LJEAAA]
     ***************************************
     LJEAAA    B    LCEAAA            * Jump when true
     ***************************************
     * Begin [Block ending at LKEAAA]
     * Return to LEEAAA with no return value
     LLEAAA    B    LCEAAA            * Jump to end of function block
     * End [Block ending at LKEAAA]
     ***************************************
     * End If [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]
     * End If [if ((c == 'D') then [Block ending at LIEAAA] else [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]]
     * End If [if ((c == 'S') then [Block ending at LHEAAA] else [if ((c == 'D') then [Block ending at LIEAAA] else [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]]]
     * End If [if ((c == 'C') then [Block ending at LGEAAA] else [if ((c == 'S') then [Block ending at LHEAAA] else [if ((c == 'D') then [Block ending at LIEAAA] else [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]]]]
     * End If [if ((c == '%') then [Block ending at LFEAAA] else [if ((c == 'C') then [Block ending at LGEAAA] else [if ((c == 'S') then [Block ending at LHEAAA] else [if ((c == 'D') then [Block ending at LIEAAA] else [if ((c == 'P') then [Block ending at LJEAAA] else [Block ending at LKEAAA]]]]]]
     * End [Block ending at LEEAAA]
     ***************************************
     * End If [if ((c != '%') then [Block ending at LDEAAA] else [Block ending at LEEAAA]]
     * End [Block ending at LCEAAA]
     ***************************************
     LCEAAA    B    LXEAAA            * Jump to top of While
     * End While [while (((c = (*(cformat_str++))) != '\0')) [Block ending at LCEAAA] top:LXEAAA bottom:LYEAAA]
     * Pop (11)
     LBEAAA    MA   LXIAAA,X2         * Add -11 to stack pointer
     * End [Block ending at LBEAAA]
     ***************************************
               LCA  3+X3,X1           * Load return address in X1
               B    0+X1              * Jump back to caller in X1
     ********************************************************************************
     * End Function : printf
     ********************************************************************************
     ********************************************************************************
     * Function : putchar
     ********************************************************************************
     LWCAAA    SBR  3+X3              * Save return address in register B in stack frame (X3)
     * Set the right WM and clear the wrong ones
               SW   1+X3              * Set WM at 1+X3
               CW   2+X3              * Clear WM at 2+X3
               CW   3+X3              * Clear WM at 3+X3
     ***************************************
     * Begin [Block ending at LXCAAA]
     * Push (3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
     * If [if ((c != '\n') then [Block ending at LYCAAA] else [Block ending at LZCAAA]]
     * NotEqual (!=) (c != '\n')
     * Parameter Variable (c : 15997+X3)
     * Push (15997+X3:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
     * Constant ('\n' : EOL)
     * Push (EOL:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  EOL,0+X2          * Load memory EOL in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
               BE   LZGAAA            * Jump if equal
               B    LAHAAA            * Jump to End
     LZGAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LAHAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LBDAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LYCAAA]
     * Parameter Variable (c : 15997+X3)
     * Push (15997+X3:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
     * PostIncrement (__putchar_pos++)
     * Push (@'24@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LYIAAA,0+X2       * Load data @'24@ in stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LYHAAA,0+X1       * Postincrement pointer at X1
     * Assignment ((*(__putchar_pos++)) = c)
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment ((*(__putchar_pos++)) = c)
     * End [Block ending at LYCAAA]
     ***************************************
     LYCAAA    B    LEDAAA            * Jump when true
     ***************************************
     * Begin [Block ending at LZCAAA]
     * While [while ((((int) __putchar_last) >= ((int) __putchar_pos))) [Block ending at LADAAA] top:LBDAAA bottom:LCDAAA]
     * Static Variable (__putchar_last : 1027)
     * Push (1027:3)
     LBDAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  1027,0+X2         * Load memory 1027 in stack
     * Cast Pointer(__putchar_last) to Number
               B    PTRNMN            * Jump to snippet pointer_to_number
     * Static Variable (__putchar_pos : 1024)
     * Push (1024:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  1024,0+X2         * Load memory 1024 in stack
     * Cast Pointer(__putchar_pos) to Number
               B    PTRNMN            * Jump to snippet pointer_to_number
     * GreaterOrEqual (((int) __putchar_last) >= ((int) __putchar_pos))
               B    CLNNMN            * Jump to snippet clean_number
               C    0+X2,15995+X2     * Compare stack to stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               MCW  LUHAAA,0+X2       * Move 1 in stack
               BL   LBHAAA            * Jump if greater or equal
               B    LCHAAA            * Jump to End
     LBHAAA    MCW  LXHAAA,0+X2       * Move 1 in stack
     LCHAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LCDAAA,5+X2,      * Jump to bottom of While
     ***************************************
     * Begin [Block ending at LADAAA]
     * Constant (' ' : @ @)
     * Push (@ @:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  LZIAAA,0+X2       * Load data @ @ in stack
     * PostDecrement (__putchar_last--)
     * Push (@'27@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LAJAAA,0+X2       * Load data @'27@ in stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LBIAAA,0+X1       * Postdecrement pointer at X1
     * Assignment ((*(__putchar_last--)) = ' ')
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:1)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * End Assignment ((*(__putchar_last--)) = ' ')
     * End [Block ending at LADAAA]
     ***************************************
     LADAAA    B    LBDAAA            * Jump to top of While
     * End While [while ((((int) __putchar_last) >= ((int) __putchar_pos))) [Block ending at LADAAA] top:LBDAAA bottom:LCDAAA]
     * Static Variable (__putchar_pos : 1024)
     * Push (1024:3)
     LCDAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  1024,0+X2         * Load memory 1024 in stack
     * Push (@'27@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LAJAAA,0+X2       * Load data @'27@ in stack
     * Assignment (__putchar_last = __putchar_pos)
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (__putchar_last = __putchar_pos)
     * Constant (201 : @201@)
     * Push (@201@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LBJAAA,0+X2       * Load data @201@ in stack
     * Push (@'24@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LYIAAA,0+X2       * Load data @'24@ in stack
     * Assignment (__putchar_pos = 201)
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (__putchar_pos = 201)
     * Start asm block
               W                      * Start asm block
     * End asm block
     * End [Block ending at LZCAAA]
     ***************************************
     * End If [if ((c != '\n') then [Block ending at LYCAAA] else [Block ending at LZCAAA]]
     * If [if ((__putchar_pos == 333) then [Block ending at LFDAAA]]
     * Equal (__putchar_pos == 333)
     * Static Variable (__putchar_pos : 1024)
     * Push (1024:3)
     LEDAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  1024,0+X2         * Load memory 1024 in stack
     * Constant (333 : @333@)
     * Push (@333@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LCJAAA,0+X2       * Load data @333@ in stack
               C    0+X2,15997+X2     * Compare stack to stack at -3
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (@00000@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LXHAAA,0+X2       * Load data @00000@ in stack
               BE   LDHAAA            * Jump if equal
               B    LEHAAA            * Jump to End
     LDHAAA    MCW  LUHAAA,0+X2       * Move 1 in stack
     LEHAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LXCAAA,5+X2,      * Jump when False
     ***************************************
     * Begin [Block ending at LFDAAA]
     * Static Variable (__putchar_pos : 1024)
     * Push (1024:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  1024,0+X2         * Load memory 1024 in stack
     * Push (@'27@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LAJAAA,0+X2       * Load data @'27@ in stack
     * Assignment (__putchar_last = __putchar_pos)
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (__putchar_last = __putchar_pos)
     * Constant (201 : @201@)
     * Push (@201@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LBJAAA,0+X2       * Load data @201@ in stack
     * Push (@'24@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LYIAAA,0+X2       * Load data @'24@ in stack
     * Assignment (__putchar_pos = 201)
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (0+X1:3)
               LCA  0+X2,0+X1         * Load stack in 0+X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * End Assignment (__putchar_pos = 201)
     * Start asm block
               W                      * Start asm block
     * End asm block
     * End [Block ending at LFDAAA]
     ***************************************
     * End If [if ((__putchar_pos == 333) then [Block ending at LFDAAA]]
     * Pop (3)
     LXCAAA    MA   LSHAAA,X2         * Add -3 to stack pointer
     * End [Block ending at LXCAAA]
     ***************************************
               LCA  3+X3,X1           * Load return address in X1
               B    0+X1              * Jump back to caller in X1
     ********************************************************************************
     * End Function : putchar
     ********************************************************************************
     ********************************************************************************
     * Function : puts
     ********************************************************************************
     LHDAAA    SBR  3+X3              * Save return address in register B in stack frame (X3)
     * Set the right WM and clear the wrong ones
               SW   1+X3              * Set WM at 1+X3
               CW   2+X3              * Clear WM at 2+X3
               CW   3+X3              * Clear WM at 3+X3
     ***************************************
     * Begin [Block ending at LIDAAA]
     * Push (3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
     * While [while (((*s) != '\0')) [Block ending at LJDAAA] top:LKDAAA bottom:LLDAAA]
     * NotEqual (!=) ((*s) != '\0')
     * DereferenceExpression (*s)
     * Parameter Variable (s : 15997+X3)
     * Push (15997+X3:3)
     LKDAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*s)
     * Constant ('\0' : EOS)
     * Push (EOS:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  EOS,0+X2          * Load memory EOS in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
               BE   LFHAAA            * Jump if equal
               B    LGHAAA            * Jump to End
     LFHAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LGHAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LIDAAA,5+X2,      * Jump to bottom of While
     ***************************************
     * Begin [Block ending at LJDAAA]
     * Function Call putchar((*(s++)))
     * Push (5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
     * DereferenceExpression (*(s++))
     * PostIncrement (s++)
     * Push (@I9G@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LYHAAA,0+X1       * Postincrement pointer at X1
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*(s++))
     * Create a stack frame with X3 pointer to it
     * Push (X3:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  X3,0+X2           * Load X3 in stack
               MCW  X2,X3             * Move X2 in X3
               B    LWCAAA            * Jump to function putchar
     * Pop (X3:3)
               LCA  0+X2,X3           * Load stack in X3
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * End Function Call putchar((*(s++)))
     * End [Block ending at LJDAAA]
     ***************************************
     LJDAAA    B    LKDAAA            * Jump to top of While
     * End While [while (((*s) != '\0')) [Block ending at LJDAAA] top:LKDAAA bottom:LLDAAA]
     * Pop (3)
     LIDAAA    MA   LSHAAA,X2         * Add -3 to stack pointer
     * End [Block ending at LIDAAA]
     ***************************************
               LCA  3+X3,X1           * Load return address in X1
               B    0+X1              * Jump back to caller in X1
     ********************************************************************************
     * End Function : puts
     ********************************************************************************
     ********************************************************************************
     * Function : strcpy
     ********************************************************************************
     LJAAAA    SBR  3+X3              * Save return address in register B in stack frame (X3)
     * Set the right WM and clear the wrong ones
               SW   1+X3              * Set WM at 1+X3
               CW   2+X3              * Clear WM at 2+X3
               CW   3+X3              * Clear WM at 3+X3
     ***************************************
     * Begin [Block ending at LKAAAA]
     * Push (3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
     * While [while ((((*(dest++)) = (*(src++))) != '\0')) [] top:LLAAAA bottom:LMAAAA]
     * NotEqual (!=) (((*(dest++)) = (*(src++))) != '\0')
     * DereferenceExpression (*(src++))
     * PostIncrement (src++)
     * Push (@I9D@:3)
     LLAAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LDJAAA,0+X2       * Load data @I9D@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LYHAAA,0+X1       * Postincrement pointer at X1
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * End DereferenceExpression (*(src++))
     * PostIncrement (dest++)
     * Push (@I9G@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LSHAAA,0+X2       * Load data @I9G@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
               MA   LYHAAA,0+X1       * Postincrement pointer at X1
     * Assignment ((*(dest++)) = (*(src++)))
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
               LCA  0+X2,0+X1         * Load stack in memory X1
     * End Assignment ((*(dest++)) = (*(src++)))
     * Constant ('\0' : EOS)
     * Push (EOS:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  EOS,0+X2          * Load memory EOS in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
               BE   LHHAAA            * Jump if equal
               B    LIHAAA            * Jump to End
     LHHAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LIHAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LKAAAA,5+X2,      * Jump to bottom of While
               B    LLAAAA            * Jump to top of While
     * End While [while ((((*(dest++)) = (*(src++))) != '\0')) [] top:LLAAAA bottom:LMAAAA]
     * Pop (3)
     LKAAAA    MA   LSHAAA,X2         * Add -3 to stack pointer
     * End [Block ending at LKAAAA]
     ***************************************
               LCA  3+X3,X1           * Load return address in X1
               B    0+X1              * Jump back to caller in X1
     ********************************************************************************
     * End Function : strcpy
     ********************************************************************************
     ********************************************************************************
     * Function : strlen
     ********************************************************************************
     LFAAAA    SBR  3+X3              * Save return address in register B in stack frame (X3)
     * Set the right WM and clear the wrong ones
               SW   1+X3              * Set WM at 1+X3
               CW   2+X3              * Clear WM at 2+X3
               CW   3+X3              * Clear WM at 3+X3
     ***************************************
     * Begin [Block ending at LGAAAA]
     * len size:5 offset:3
               LCA  LEJAAA,8+X3       * Load int -1 into memory 8+X3
     * Push (8)
               MA   LEIAAA,X2         * Add 8 to stack pointer
     * While [while (((str[(++len)]) != '\0')) [] top:LHAAAA bottom:LIAAAA]
     * NotEqual (!=) ((str[(++len)]) != '\0')
     * SubScript (str[(++len)])
     * Parameter Variable (str : 15997+X3)
     * Push (15997+X3:3)
     LHAAAA    MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  15997+X3,0+X2     * Load memory 15997+X3 in stack
     * PreIncrement((++len)
     * Push (@008@:3)
               MA   LLHAAA,X2         * Add 3 to stack pointer
               LCA  LEIAAA,0+X2       * Load data @008@ in stack
               MA   X3,0+X2           * Add X3 to stack
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
               A    LUHAAA,0+X1       * Preincrement memory at X1
     * Push (0+X1:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * Put raw index on the stack
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
               M    15995+X2,6+X2     * Multiply stack at -5 to stack at 6
               SW   2+X2              * Set WM in stack at 2
               LCA  6+X2,15995+X2     * Load stack at 6 in stack at -5
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Stack top is now array index
               B    NMNPTR            * Jump to snippet number_to_pointer
               MA   0+X2,15997+X2     * Add stack to stack at -3
     * Pop (3)
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Stack top is location in array now
     * End SubScript (str[(++len)])
     * Pop (X1:3)
               LCA  0+X2,X1           * Load stack in X1
               MA   LSHAAA,X2         * Add -3 to stack pointer
     * Push (0+X1:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  0+X1,0+X2         * Load memory 0+X1 in stack
     * Constant ('\0' : EOS)
     * Push (EOS:1)
               MA   LYHAAA,X2         * Add 1 to stack pointer
               LCA  EOS,0+X2          * Load memory EOS in stack
               C    0+X2,15999+X2     * Compare stack to stack at -1
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Pop (1)
               MA   LBIAAA,X2         * Add -1 to stack pointer
     * Push (@00001@:5)
               MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  LUHAAA,0+X2       * Load data @00001@ in stack
               BE   LJHAAA            * Jump if equal
               B    LKHAAA            * Jump to End
     LJHAAA    MCW  LXHAAA,0+X2       * Move 0 in stack
     LKHAAA    MCS  0+X2,0+X2         * Clear WM in stack
     * Pop (5)
               MA   LPHAAA,X2         * Add -5 to stack pointer
               BCE  LIAAAA,5+X2,      * Jump to bottom of While
               B    LHAAAA            * Jump to top of While
     * End While [while (((str[(++len)]) != '\0')) [] top:LHAAAA bottom:LIAAAA]
     * Put on stack return value (len)
     * Local Variable (len : 8+X3)
     * Push (8+X3:5)
     LIAAAA    MA   LMHAAA,X2         * Add 5 to stack pointer
               LCA  8+X3,0+X2         * Load memory 8+X3 in stack
     * Pop (15994+X3:5)
               LCA  0+X2,15994+X3     * Load stack in 15994+X3
               MA   LPHAAA,X2         * Add -5 to stack pointer
     * Pop (8)
     LGAAAA    MA   LAIAAA,X2         * Add -8 to stack pointer
     * End [Block ending at LGAAAA]
     ***************************************
               LCA  3+X3,X1           * Load return address in X1
               B    0+X1              * Jump back to caller in X1
     ********************************************************************************
     * End Function : strlen
     ********************************************************************************
****************************************************************** CAST NUMBER TO POINTER SNIPPET                             ******************************************************************     NMNPTR    SBR  X1
* Casts a 5-digit number to a 3-digit address* make a copy of the top of the stack               SW   15998+X2
               LCA  0+X2,3+X2
               CW   15998+X2
* zero out the zone bits of our copy               MZ   @0@,3+X2
               MZ   @0@,2+X2
               MZ   @0@,1+X2
* set the low-order digit's zone bits               C    @04000@,0+X2
               BL   NPHIGH
               C    @08000@,0+X2
               BL   NPLOZ
               C    @12000@,0+X2
               BL   NPLZO
               S    @12000@,0+X2
               MZ   @A@,3+X2
               B    NPHIGH
     NPLZO     S    @08000@,0+X2
               MZ   @I@,3+X2
               B    NPHIGH
     NPLOZ     S    @04000@,0+X2
               MZ   @S@,3+X2
* For some reason the zone bits get set - it still works though.     NPHIGH    C    @01000@,0+X2
               BL   NMPTRE
               C    @02000@,0+X2
               BL   NPHOZ
               C    @03000@,0+X2
               BL   NPHZO
               MZ   @A@,1+X2
               B    NMPTRE
     NPHZO     MZ   @I@,1+X2
               B    NMPTRE
     NPHOZ     MZ   @S@,1+X2
     NMPTRE    LCA  3+X2,15998+X2
               SBR  X2,15998+X2
               B    0+X1
********************************************************************************************************************************** DIVISION SNIPPET                                           ******************************************************************* SETUP RETURN ADDRESS     SNPDIV    SBR  DIVEND+3
* POP DIVIDEND               MCW  0+X2,CDIV2
               SBR  X2,15995+X2
* POP DIVISOR               MCW  0+X2,CDIV1
               SBR  X2,15995+X2
               B    *+17              * Branch 17 places down?
               DCW  @00000@
               DC   @00000000000@
               ZA   CDIV2,*-7         * PUT DIVIDEND INTO WORKING BL
               D    CDIV1,*-19        * DIVIDE
               MZ   *-22,*-21         * KILL THE ZONE BIT
               MZ   *-29,*-34         * KILL THE ZONE BIT
               MCW  *-41,CDIV3        * PICK UP ANSWER
               SW   *-44              * SO I CAN PICKUP REMAINDER
               MCW  *-46,CDIV4        * GET REMAINDER
               CW   *-55              * CLEAR THE WM
               MZ   CDIV3-1,CDIV3     * CLEANUP QUOTIENT BITZONE
               MZ   CDIV4-1,CDIV4     * CLEANUP REMAINDER BITZONE
* PUSH REMAINDER               SBR  X2,5+X2
               SW   15996+X2
               MCW  CDIV4,0+X2
* PUSH QUOTIENT               SBR  X2,5+X2
               SW   15996+X2
               MCW  CDIV3,0+X2
* JUMP BACK     DIVEND    B    000
* DIVISOR     CDIV1     DCW  00000
* DIVIDEND     CDIV2     DCW  00000
* QUOTIENT     CDIV3     DCW  00000
* REMAINDER     CDIV4     DCW  00000
********************************************************************************************************************************** CAST POINTER TO NUMBER SNIPPET                             ******************************************************************     PTRNMN    SBR  X1
* Casts a 3-digit address to a 5-digit number* Make room on the stack for an int               MA   @002@,X2
* make a copy of the top of the stack               LCA  15998+X2,3+X2
* Now zero out the top of the stack               LCA  @00000@,0+X2
* Now copy back, shifted over 2 digits               MCW  3+X2,0+X2
* Now zero out the zone bits on the stack               MZ   @0@,0+X2
               MZ   @0@,15999+X2
               MZ   @0@,15998+X2
* check the high-order digit's zone bits               BWZ  PNHOZ,1+X2,S
               BWZ  PNHZO,1+X2,K
               BWZ  PNHOO,1+X2,B
               B    PNLOW
     PNHOZ     A    @01000@,0+X2
               B    PNLOW
     PNHZO     A    @02000@,0+X2
               B    PNLOW
     PNHOO     A    @03000@,0+X2
     PNLOW     BWZ  PNLOZ,3+X2,S
               BWZ  PNLZO,3+X2,K
               BWZ  PNLOO,3+X2,B
               B    PTRNME
     PNLOZ     A    @04000@,0+X2
               B    PTRNME
     PNLZO     A    @08000@,0+X2
               B    PTRNME
     PNLOO     A    @12000@,0+X2
     PTRNME    B    0+X1
********************************************************************************************************************************** CLEAN NUMBER SNIPPET                                       ******************************************************************* Normalizes the zone bits of a number, leaving either A=0B=0* for a positive or A=0B=1 for a negative     CLNNMN    SBR  X1
* Do nothing on either no zone bits or only a b zone bit               BWZ  CLNNME,0+X2,2
               BWZ  CLNNME,0+X2,K
* else clear the zone bits, as it is positive               MZ   @,@,0+X2
     CLNNME    B    0+X1
****************************************************************     LBIAAA    DCW  @I9I@
     LNHAAA    DCW  @100000@
     LXIAAA    DCW  @I8I@
     LIIAAA    DCW  @'31@
     LLIAAA    DCW  @'53@
     LJIAAA    DCW  @'41@
     LEJAAA    DCW  @0000J@
     LUIAAA    DCW  @I9C@
     LPHAAA    DCW  @I9E@
     LTHAAA    DCW  @'05@
     LAJAAA    DCW  @'27@
     LQIAAA    DCW  @S@
     LWIAAA    DCW  @010@
     LSHAAA    DCW  @I9G@
     LKIAAA    DCW  @'47@
     LCIAAA    DCW  @0@
     LCJAAA    DCW  @333@
     LVHAAA    DCW  @014@
     LTIAAA    DCW  @012@
     LEIAAA    DCW  @008@
     LWHAAA    DCW  @006@
     LHIAAA    DCW  @00005@
     LQHAAA    DCW  @00019@
     LUHAAA    DCW  @00001@
     LFIAAA    DCW  @00100@
     LPIAAA    DCW  @C@
     LGIAAA    DCW  @00003@
     LOHAAA    DCW  @00042@
     LZIAAA    DCW  @ @
     LXHAAA    DCW  @00000@
     LSIAAA    DCW  @00010@
     LAIAAA    DCW  @I9B@
     LDJAAA    DCW  @I9D@
     LMIAAA    DCW  @011@
     LRHAAA    DCW  @'04@
     LDIAAA    DCW  @I8F@
     LYIAAA    DCW  @'24@
     LVIAAA    DCW  @P@
     LBJAAA    DCW  @201@
     LLHAAA    DCW  @003@
     LYHAAA    DCW  @001@
     LZHAAA    DCW  @-@
     LNIAAA    DCW  @007@
     LMHAAA    DCW  @005@
     LOIAAA    DCW  @%@
     LRIAAA    DCW  @D@
               END  START             * End of program code.
