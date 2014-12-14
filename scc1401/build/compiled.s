     ****************************************************************

     READ      EQU  001                * Read area
     PUNCH     EQU  101                * Punch area
     PRINT     EQU  201                * Print area
     
     PRCPOS    DCW  000                * char position in print area
     PUCPOS    DCW  000                * char position in punch area
     PUNSIZ    DCW  @080@              * Size of punch area
     PRTSIZ    DCW  @132@              * Size of print area
     EOS       DCW  @'@                * End Of String char (string terminator)
     EOL       DCW  @;@                * End Of Line char

               ORG  87
     X1        DSA  0                  * INDEX REGISTER 1
               ORG  92
     X2        DSA  0                  * INDEX REGISTER 2
               ORG  97
     X3        DSA  0                  * INDEX REGISTER 3

               ORG  6000
  
     START     NOP
     
     ****************************************************************  
     
               SBR  X2, 400            * SET THE STACK
               MCW  X2, X3
               SW   1001
               MCW  @00001@,1005
               SW   1006
               MCW  @00000@,1010
               B    LBAAAA
               H    
     LAAAAA    SBR  3+X3
               SW   1+X3
               MA   @003@,X2
     * Push(15997+X3:5)
               SW   1+X2
               MA   @005@,X2
               MCW  15997+X3,0+X2
               MCS  0+X2,0+X2
     * Pop(5)
               MA   @I9E@,X2
               CW   1+X2
               BCE  LFAAAA,5+X2, 
     * Push(1005:5)
               SW   1+X2
               MA   @005@,X2
               MCW  1005,0+X2
     * Push(15997+X3:5)
               SW   1+X2
               MA   @005@,X2
               MCW  15997+X3,0+X2
               M    15995+X2,6+X2
               SW   2+X2
               MCW  6+X2,15995+X2
               CW   2+X2
     * Pop(5)
               MA   @I9E@,X2
               CW   1+X2
     * Push(@'05@:3)
               SW   1+X2
               MA   @003@,X2
               MCW  @'05@,0+X2
     * Pop(X1:3)
               MCW  0+X2,X1
               MA   @I9G@,X2
               CW   1+X2
     * Pop(0+X1:5)
               MCW  0+X2,0+X1
               MA   @I9E@,X2
               CW   1+X2
     * Push(5)
               SW   1+X2
               MA   @005@,X2
     * Push(15997+X3:5)
               SW   1+X2
               MA   @005@,X2
               MCW  15997+X3,0+X2
     * Push(@00001@:5)
               SW   1+X2
               MA   @005@,X2
               MCW  @00001@,0+X2
               S    0+X2,15995+X2
     * Pop(5)
               MA   @I9E@,X2
               CW   1+X2
     * Push(X3:3)
               SW   1+X2
               MA   @003@,X2
               MCW  X3,0+X2
               MCW  X2,X3
               B    LAAAAA
     * Pop(X3:3)
               MCW  0+X2,X3
               MA   @I9G@,X2
               CW   1+X2
     * Pop(5)
               MA   @I9E@,X2
               CW   1+X2
     * Pop(5)
               MA   @I9E@,X2
               CW   1+X2
     LFAAAA    NOP  
               MA   @I9G@,X2
               CW   1+X3
               MCW  3+X3,X1
               B    0+X1
     LBAAAA    SBR  3+X3
               SW   1+X3
               SW   4+X3
               MCW  @00000@,8+X3
               SW   9+X3
               SW   14+X3
               MCW  @00201@,16+X3
               MA   @016@,X2
     * Push(@00010@:5)
               SW   1+X2
               MA   @005@,X2
               MCW  @00010@,0+X2
     * Push(@013@:3)
               SW   1+X2
               MA   @003@,X2
               MCW  @013@,0+X2
               MA   X3,0+X2
     * Pop(X1:3)
               MCW  0+X2,X1
               MA   @I9G@,X2
               CW   1+X2
     * Pop(0+X1:5)
               MCW  0+X2,0+X1
               MA   @I9E@,X2
               CW   1+X2
     LCAAAA    NOP  
     * Push(13+X3:5)
               SW   1+X2
               MA   @005@,X2
               MCW  13+X3,0+X2
               MCS  0+X2,0+X2
     * Pop(5)
               MA   @I9E@,X2
               CW   1+X2
               BCE  LDAAAA,5+X2, 
     * Push(@A@:1)
               SW   1+X2
               MA   @001@,X2
               MCW  @A@,0+X2
     * Push(16+X3:3)
               SW   1+X2
               MA   @003@,X2
               MCW  16+X3,0+X2
     * Push(13+X3:5)
               SW   1+X2
               MA   @005@,X2
               MCW  13+X3,0+X2
     * raw index on the stack
     * Push(@00001@:5)
               SW   1+X2
               MA   @005@,X2
               MCW  @00001@,0+X2
               M    15995+X2,6+X2
               SW   2+X2
               MCW  6+X2,15995+X2
               CW   2+X2
     * Pop(5)
               MA   @I9E@,X2
               CW   1+X2
     * STACK TOP IS NOW ARRAY INDEX
               B    LGAAAA
               MA   0+X2,15997+X2
     * Pop(3)
               MA   @I9G@,X2
               CW   1+X2
     * STACK top is location in array now.
     * Pop(X1:3)
               MCW  0+X2,X1
               MA   @I9G@,X2
               CW   1+X2
     * Pop(0+X1:1)
               MCW  0+X2,0+X1
               MA   @I9I@,X2
               CW   1+X2
     LEAAAA    NOP  
     * Push(@013@:3)
               SW   1+X2
               MA   @003@,X2
               MCW  @013@,0+X2
               MA   X3,0+X2
     * Pop(X1:3)
               MCW  0+X2,X1
               MA   @I9G@,X2
               CW   1+X2
               S    @00001@,0+X1
               B    LCAAAA
     LDAAAA    NOP  
     * Push(5)
               SW   1+X2
               MA   @005@,X2
     * Push(@00008@:5)
               SW   1+X2
               MA   @005@,X2
               MCW  @00008@,0+X2
     * Push(X3:3)
               SW   1+X2
               MA   @003@,X2
               MCW  X3,0+X2
               MCW  X2,X3
               B    LAAAAA
     * Pop(X3:3)
               MCW  0+X2,X3
               MA   @I9G@,X2
               CW   1+X2
     * Pop(5)
               MA   @I9E@,X2
               CW   1+X2
     * Push(@008@:3)
               SW   1+X2
               MA   @003@,X2
               MCW  @008@,0+X2
               MA   X3,0+X2
     * Pop(X1:3)
               MCW  0+X2,X1
               MA   @I9G@,X2
               CW   1+X2
     * Pop(0+X1:5)
               MCW  0+X2,0+X1
               MA   @I9E@,X2
               CW   1+X2
               MA   @I8D@,X2
               CW   1+X3
               CW   4+X3
               CW   9+X3
               CW   14+X3
               MCW  3+X3,X1
               B    0+X1
     LGAAAA    SBR  X1
     * Casts a 5-digit number to a 3-digit address
     * make a copy of the top of the stack
               SW   1+X2
               MCW  0+X2,3+X2
     * zero out the zone bits of our copy
               MZ   @0@,3+X2
               MZ   @0@,2+X2
               MZ   @0@,1+X2
     * set the low-order digit's zone bits
               C    @04000@,0+X2
               BL   LJAAAA
               C    @08000@,0+X2
               BL   LIAAAA
               C    @12000@,0+X2
               BL   LHAAAA
               S    @12000@,0+X2
               MZ   @A@,3+X2
               B    LJAAAA
     LHAAAA    S    @08000@,0+X2
               MZ   @I@,3+X2
               B    LJAAAA
     LIAAAA    S    @04000@,0+X2
               MZ   @S@,3+X2
     * For some reason the zone bits get set - it still works though.
     LJAAAA    C    @01000@,0+X2
               BL   LMAAAA
               C    @02000@,0+X2
               BL   LLAAAA
               C    @03000@,0+X2
               BL   LKAAAA
               MZ   @A@,1+X2
               B    LMAAAA
     LKAAAA    MZ   @I@,1+X2
               B    LMAAAA
     LLAAAA    MZ   @S@,1+X2
     LMAAAA    MCW  3+X2,15998+X2
               CW   1+X2
               SBR  X2,15998+X2
               B    0+X1
               END  START
