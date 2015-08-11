     ****************************************************************
     ** CAST INTEGER TO CHAR SNIPPET                               **
     ****************************************************************

     NMNCHR    SBR  X2
     * Casts a 5-digit number to a 1-digit char
     * Copy the byte in last position of integer in the first position
               SW   CAST
     * Jumps back to caller
               B    0+X2

     ****************************************************************
