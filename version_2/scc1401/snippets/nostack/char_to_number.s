     ****************************************************************
     ** CAST CHAR TO INTEGER SNIPPET                               **
     ****************************************************************

     * Casts a 1-digit char to a 5-digit integer
     CHRNMN    SBR  X2                 * Save return address in
               CW   CAST               * Make char 5 digits
               LCA  @0000@,1+CAST      * Copy 4 zeros in front to fill all 5 digits
               B    0+X2               * Jump back to caller

     ****************************************************************
