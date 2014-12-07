     $MAIN     SBR  X1
     * Casts a 5-digit number to a 3-digit address
     * We will be using the top of the stack - set the word marker
               SW   15996+X2
     * make a copy of the top of the stack
               MCW  15995+X2,0+X2
     * Clear the leading digits on the stack
               MCW  @00@,15992+X2
     * set the low-order digit's zone bits
               C    @04000@,0+X2
               BL   $HIGH
               C    @08000@,0+X2
               BL   $LOZ
               C    @12000@,0+X2
               BL   $LZO
               S    @12000@,0+X2
               MZ   @A@,15995+X2          
               B    $HIGH
     $LZO      S    @08000@,0+X2
               MZ   @I@,15995+X2
               B    $HIGH
     $LOZ      S    @04000@,0+X2
               MZ   @S@,15995+X2
     * For some reason the zone bits get set - it still works though.
     $HIGH     C    @01000@,0+X2
               BL   $EXIT
               C    @02000@,0+X2
               BL   $HOZ
               C    @03000@,0+X2
               BL   $HZO
               MZ   @A@,15993+X2
               B    $EXIT
     $HZO      MZ   @I@,15993+X2
               B    $EXIT
     $HOZ      MZ   @S@,15993+X2
     $EXIT     B    0+X1
