     $MAIN     SBR  X1
     * Casts a 3-digit address to a 5-digit number
     * Make room on the stack for an int
               MA   @002@,X2
     * make a copy of the top of the stack
               SW   1+X2
               MCW  15998+X2,3+X2
     * Now zero out the top of the stack
               MCW  @00000@,0+X2
     * Now copy back, shifted over 2 digits
               MCW  3+X2,0+X2
               CW   1+X2
     * Now zero out the zone bits on the stack
               MZ   @0@,0+X2
               MZ   @0@,15999+X2
               MZ   @0@,15998+X2
     * check the high-order digit's zone bits
               BWZ  $HOZ,1+X2,S
               BWZ  $HZO,1+X2,K
               BWZ  $HOO,1+X2,B
               B    $LOW
     $HOZ      A    @01000@,0+X2
               B    $LOW
     $HZO      A    @02000@,0+X2
               B    $LOW
     $HOO      A    @03000@,0+X2
     $LOW      BWZ  $LOZ,3+X2,S
               BWZ  $LZO,3+X2,K
               BWZ  $LOO,3+X2,B
               B    $EXIT
     $LOZ      A    @04000@,0+X2
               B    $EXIT
     $LZO      A    @08000@,0+X2
               B    $EXIT
     $LOO      A    @12000@,0+X2
     $EXIT     B    0+X1
