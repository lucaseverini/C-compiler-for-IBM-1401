     ****************************************************************

     READ      EQU  001               * Read area
     PUNCH     EQU  101               * Punch area
     PRINT     EQU  201               * Print area


     PRCPOS    DCW  000               * Char position in print area
     PUCPOS    DCW  000               * Char position in punch area


     PUNSIZ    DCW  @080@             * Size of punch area
     PRTSIZ    DCW  @132@             * Size of print area


     EOS       DCW  @'@               * End of string char
     EOL       DCW  @;@               * End of line char

               ORG  87
     X1        DSA  0                 * Index reg 1

               ORG  92
     X2        DSA  0                 * Index reg 2

               ORG  97
     X3        DSA  0                 * Index reg 3

     * I need a single digit flag - should I replace this with a DA?
     RF        EQU  324
     
     ****************************************************************
