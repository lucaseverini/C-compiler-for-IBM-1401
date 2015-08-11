     ****************************************************************
     ** CAST POINTER TO NUMBER SNIPPET                             **
     ****************************************************************

     PTRNMN    SBR  X2
     * Make X1 point to CSTRES
               MCW  @449@,X1
     * Casts a 3-digit address to a 5-digit number
     * Make room on the stack for an int
     * make a copy of the top of the stack
               MCW  @00000@,15997+X1
               MCW  0+X1,15997+X1
     * Now zero out the zone bits on the stack
               MZ   @0@,15995+X1
               MZ   @0@,15996+X1
               MZ   @0@,15997+X1
     * check the high-order digit's zone bits
               BWZ  PNHOZ,15998+X1,S
               BWZ  PNHZO,15998+X1,K
               BWZ  PNHOO,15998+X1,B
               B    PNLOW
     PNHOZ     A    @01000@,15997+X1
               B    PNLOW
     PNHZO     A    @02000@,15997+X1
               B    PNLOW
     PNHOO     A    @03000@,15997+X1
     PNLOW     BWZ  PNLOZ,0+X1,S
               BWZ  PNLZO,0+X1,K
               BWZ  PNLOO,0+X1,B
               B    PTRNME
     PNLOZ     A    @04000@,15997+X1
               B    PTRNME
     PNLZO     A    @08000@,15997+X1
               B    PTRNME
     PNLOO     A    @12000@,15997+X1
     PTRNME    B    0+X2

     ****************************************************************
