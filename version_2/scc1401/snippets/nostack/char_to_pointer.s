     ****************************************************************
     ** CAST CHAR TO POINTER SNIPPET                               **
     ****************************************************************

     * Casts a 1-digit char to a 3-digit pointer
     CHRPTR    SBR  X2                 * Save return address in X1
               CW   CAST               * Clear the WM to make it an integer
               LCA  @00@,1+CAST        * Copy 2 zeros in front to fill all 3 digits
               B    0+X2               * Jump back to caller

     ****************************************************************
