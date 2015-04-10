     ****************************************************************
     ** CAST INTEGER TO CHAR SNIPPET                               **
     ****************************************************************
     
     NMNCHR    SBR  X1
     * Casts a 5-digit number to a 1-digit char
     * Copy the byte in last position of integer in the first position
               SW   0+X2
               LCA  0+X2,15996+X2
     * Make space on stack for a char instead of an int by subtracting 2 bytes to X2
               MA   @I9H@,X2
     * Jumps back to caller
               SBR  X2,15998+X2
               B    0+X1
               
     ****************************************************************
     