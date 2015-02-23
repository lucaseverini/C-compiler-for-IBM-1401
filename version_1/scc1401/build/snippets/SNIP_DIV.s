    ****************************************************************  
    ** DIVISION SNIPPET                                           **
    ****************************************************************  
     
     $MAIN     SBR  $XDIV5+3           * SETUP RETURN ADDRESS
     * POP DIVIDEND
               MCW  0+X2, $XDIV2
               SBR  X2, 15995+X2

     * POP DIVISOR
               MCW  0+X2, $XDIV1
               SBR  X2, 15995+X2


               B    *+17
               
               DCW  @00000@                
               DC   @00000000000@        

               ZA   $XDIV2, *-7         * PUT DIVIDEND INTO WORKING BL
               D    $XDIV1, *-19        * DIVIDE
               MZ   *-22, *-21          * KILL THE ZONE BIT
               MZ   *-29, *-34          * KILL THE ZONE BIT
               MCW  *-41, $XDIV3        * PICK UP ANSWER
               SW   *-44                * SO I CAN PICKUP REMAINDER
               MCW  *-46, $XDIV4        * GET REMAINDER
               CW   *-55                * CLEAR THE WM
               MZ   $XDIV3-1, $XDIV3    * CLEANUP QUOTIENT BITZONE
               MZ   $XDIV4-1, $XDIV4    * CLEANUP REMAINDER BITZONE
               
     * PUSH REMAINDER
               SBR  X2, 5+X2
               SW   15996+X2
               MCW  $XDIV4, 0+X2
               
     * PUSH QUOTIENT
               SBR  X2, 5+X2
               SW   15996+X2
               MCW  $XDIV3, 0+X2

     $XDIV5    B    000                 * JUMP BACK
               
     $XDIV1    DCW  00000               * DIVISOR
     $XDIV2    DCW  00000               * DIVIDEND
     $XDIV3    DCW  00000               * QUOTIENT
     $XDIV4    DCW  00000               * REMAINDER
