     ****************************************************************
     ** CAST CHAR TO INTEGER SNIPPET                               **
     ****************************************************************
     
     * Casts a 1-digit char to a 5-digit integer
     CHRNMN    SBR  X1                 * Save return address in X1
               
               MA   @002@,X2           * Make room on the stack for an int 
               LCA  15998+X2,2+X2      * Copy only the first digit
               LCA  @0000@,1+X2        * Copy 4 zeros in front to fill all 5 digits
               CW   2+X2               * Clear the WM to make it an integer
               
               SBR  X2,2+X2            * ???
               B    0+X1               * Jump back to caller
               
     ****************************************************************
