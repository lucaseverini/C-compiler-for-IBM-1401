     ****************************************************************
     ** CAST NUMBER TO POINTER SNIPPET                             **
     ****************************************************************

     NMNPTR    SBR  X2
     * Casts a 5-digit number to a 3-digit address
     * make a copy of the top of the stack
               MCW  CAST,CSTRES
     * Make X1 point to CSTRES
               MCW  @449@,X1
     * zero out the zone bits of our copy
               MZ   @0@,0+X1
               MZ   @0@,15999+X1
               MZ   @0@,15998+X1
     * set the low-order digit's zone bits
               C    @04000@,CAST
               BL   NPHIGH
               C    @08000@,CAST
               BL   NPLOZ
               C    @12000@,CAST
               BL   NPLZO
               S    @12000@,CAST
               MZ   @A@,0+X1
               B    NPHIGH
     NPLZO     S    @08000@,CAST
               MZ   @J@,0+X1
               B    NPHIGH
     NPLOZ     S    @04000@,CAST
               MZ   @S@,0+X1
     * For some reason the zone bits get set - it still works though.
     NPHIGH    C    @01000@,CAST
               BL   NMPTRE
               C    @02000@,CAST
               BL   NPHOZ
               C    @03000@,CAST
               BL   NPHZO
               MZ   @A@,15998+X1
               B    NMPTRE
     NPHZO     MZ   @I@,15998+X1
               B    NMPTRE
     NPHOZ     MZ   @S@,15998+X1
     NMPTRE    B    0+X2

     ****************************************************************
