     $MAIN     SBR  X1
     * Casts a 5-digit number to a 3-digit address
     * make a copy of the top of the stack
               SW   1+X2
               MCW  0+X2,3+X2
     * set the low-order digit's zone bits
               C    @04000@,0+X2
               BL   $HIGH
               C    @08000@,0+X2
               BL   $LOZ
               C    @12000@,0+X2
               BL   $LZO
               S    @12000@,0+X2
               MZ   @A@,3+X2
               B    $HIGH
     $LZO      S    @08000@,0+X2
               MZ   @I@,3+X2
               B    $HIGH
     $LOZ      S    @04000@,0+X2
               MZ   @S@,3+X2
     * For some reason the zone bits get set - it still works though.
     $HIGH     C    @01000@,0+X2
               BL   $EXIT
               C    @02000@,0+X2
               BL   $HOZ
               C    @03000@,0+X2
               BL   $HZO
               MZ   @A@,1+X2
               B    $EXIT
     $HZO      MZ   @I@,1+X2
               B    $EXIT
     $HOZ      MZ   @S@,1+X2
     $EXIT     MCW  3+X2,15998+X2
               CW   1+X2
               SBR  X2,15998+X2
               B    0+X1
