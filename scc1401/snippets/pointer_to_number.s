     $MAIN     SBR  X1
     * We will be using the top of the stack - set the word marker
               SW   15996+X2
     * Casts a 3-digit address to a 5-digit number
     * first make sure the address on the stack has leading zeroes
     *         MCW  @00@,15992+X2
     * make a copy of the top of the stack
               MCW  15995+X2,0+X2
     * Now zero out the zone bits on the stack
               MZ   @000@,15995+X2
               MZ   @000@,15994+X2
               MZ   @000@,15993+X2
     * check the high-order digit's zone bits
               BWZ  $HOZ,15998+X2,S
               BWZ  $HZO,15998+X2,K
               BWZ  $HOO,15998+X2,B
               B    $LOW
     $HOZ      A    @1000@,15995+X2
               B    $LOW
     $HZO      A    @2000@,15995+X2
               B    $LOW
     $HOO      A    @3000@,15995+X2
     $LOW      BWZ  $LOZ,0+X2,S
               BWZ  $LZO,0+X2,K
               BWZ  $LOO,0+X2,B
               B    $EXIT
     $LOZ      A    @4000@,15995+X2
               B    $EXIT
     $LZO      A    @8000@,15995+X2
               B    $EXIT
     $LOO      A    @12000@,15995+X2
     $EXIT     B    0+X1
