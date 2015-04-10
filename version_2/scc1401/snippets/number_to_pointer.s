     ****************************************************************
     ** CAST NUMBER TO POINTER SNIPPET                             **
     ****************************************************************

     NMNPTR    SBR  X1
     * Casts a 5-digit number to a 3-digit address
     * make a copy of the top of the stack
               SW   15998+X2
               LCA  0+X2,3+X2
               CW   15998+X2
     * zero out the zone bits of our copy
               MZ   @0@,3+X2
               MZ   @0@,2+X2
               MZ   @0@,1+X2
     * set the low-order digit's zone bits
               C    @04000@,0+X2
               BL   NPHIGH
               C    @08000@,0+X2
               BL   NPLOZ
               C    @12000@,0+X2
               BL   NPLZO
               S    @12000@,0+X2
               MZ   @A@,3+X2
               B    NPHIGH
     NPLZO     S    @08000@,0+X2
               MZ   @I@,3+X2
               B    NPHIGH
     NPLOZ     S    @04000@,0+X2
               MZ   @S@,3+X2
     * For some reason the zone bits get set - it still works though.
     NPHIGH    C    @01000@,0+X2
               BL   NMPTRE
               C    @02000@,0+X2
               BL   NPHOZ
               C    @03000@,0+X2
               BL   NPHZO
               MZ   @A@,1+X2
               B    NMPTRE
     NPHZO     MZ   @I@,1+X2
               B    NMPTRE
     NPHOZ     MZ   @S@,1+X2
     NMPTRE    LCA  3+X2,15998+X2
               SBR  X2,15998+X2
               B    0+X1

     ****************************************************************
