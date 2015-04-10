     ****************************************************************
     ** CAST CHAR TO INTEGER SNIPPET                               **
     ****************************************************************
     
     * Casts a 1-digit char to a 5-digit integer
     CHRNMN    SBR  X1
     * Make room on the stack for an int
               MA   @002@,X2
               LCA  15998+X2,2+X2
               LCA  @0000@,1+X2
               
               SBR  X2,2+X2
               B    0+X1
               
     ****************************************************************
