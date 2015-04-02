     ****************************************************************
     * Read area
     READ      EQU  001
     * Punch area
     PUNCH     EQU  101
     * Print area
     PRINT     EQU  201

     * Char position in print area
     PRCPOS    DCW  000
     * Char position in punch area
     PUCPOS    DCW  000

     * Size of punch area
     PUNSIZ    DCW  @080@
     * Size of print area
     PRTSIZ    DCW  @132@

     * End of string char
     EOS       DCW  @'@
     * End of line char
     EOL       DCW  @;@

     * Index reg 1
               ORG  87
     X1        DSA  0

     * Index reg 2
               ORG  92
     X2        DSA  0

     * Index reg 3
               ORG  97
     X3        DSA  0

     * I need a single digit flag - should I replace this with a DA?
     RF        EQU  324
     
     ****************************************************************
