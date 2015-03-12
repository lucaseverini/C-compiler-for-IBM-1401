     ****************************************************************

     READ      EQU  001                * Read area
     PUNCH     EQU  101                * Punch area
     PRINT     EQU  201                * Print area
     
     PRCPOS    DCW  000                * char position in print area
     PUCPOS    DCW  000                * char position in punch area
     PUNSIZ    DCW  @080@              * Size of punch area
     PRTSIZ    DCW  @132@              * Size of print area
     EOS       DCW  @'@                * End Of String char
     EOL       DCW  @;@                * End Of Line char

               ORG  87
     X1        DSA  0                  * INDEX REGISTER 1
               ORG  92
     X2        DSA  0                  * INDEX REGISTER 2
               ORG  97
     X3        DSA  0                  * INDEX REGISTER 3
     
     * I need a single digit flag - should I replace this with a DA?
     RF        EQU  340
     
     ****************************************************************
     