     ****************************************************************
     ** CAST CHAR TO POINTER SNIPPET                               **
     ****************************************************************

     * Casts a 1-digit char to a 3-digit pointer
     CHRPTR    SBR  X1                 * Save return address in X1

               LCA  0+X2,2+X2          * Copy only the first digit
               LCA  @00@,1+X2          * Copy 2 zeros in front to fill all 3 digits
               CW   2+X2               * Clear the WM to make it an integer

               SBR  X2,2+X2            * ???
               B    0+X1               * Jump back to caller

     ****************************************************************
