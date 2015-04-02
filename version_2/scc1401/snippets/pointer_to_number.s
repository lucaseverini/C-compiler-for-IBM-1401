     ****************************************************************
     ** CAST POINTER TO NUMBER SNIPPET                             **
     ****************************************************************

     PTRNUM    SBR  X1
     * Casts a 3-digit address to a 5-digit number
     * Make room on the stack for an int
               MA   @002@,X2
     * make a copy of the top of the stack
               LCA  15998+X2,3+X2
     * Now zero out the top of the stack
               LCA  @00000@,0+X2
     * Now copy back, shifted over 2 digits
               MCW  3+X2,0+X2
     * Now zero out the zone bits on the stack
               MZ   @0@,0+X2
               MZ   @0@,15999+X2
               MZ   @0@,15998+X2
     * check the high-order digit's zone bits
               BWZ  PNHOZ,1+X2,S
               BWZ  PNHZO,1+X2,K
               BWZ  PNHOO,1+X2,B
               B    PNLOW
     PNHOZ     A    @01000@,0+X2
               B    PNLOW
     PNHZO     A    @02000@,0+X2
               B    PNLOW
     PNHOO     A    @03000@,0+X2
     PNLOW     BWZ  PNLOZ,3+X2,S
               BWZ  PNLZO,3+X2,K
               BWZ  PNLOO,3+X2,B
               B    PTRNME
     PNLOZ     A    @04000@,0+X2
               B    PTRNME
     PNLZO     A    @08000@,0+X2
               B    PTRNME
     PNLOO     A    @12000@,0+X2
     PTRNME    B    0+X1
     
     ****************************************************************
