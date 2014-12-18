    ****************************************************************  
    ** DIVISION SNIPPET                                           **
    ****************************************************************  
     
     $-DIV     SBR  $-DIV05+3           * SETUP RETURN ADDRESS
               
     * POP DIVIDEND
               SBR  X2, 15995+X2
               MCW  0+X2, $-DIV02

     * POP DIVISOR
               SBR  X2, 15995+X2
               MCW  0+X2, $-DIV01

               B    *+17
               
               DCW  @00000@                
               DC   @00000000000@        

               ZA   $-DIV02, *-7        * PUT DIVIDEND INTO WORKING BL
               D    $-DIV01, *-19       * DIVIDE
               MZ   *-22, *-21          * KILL THE ZONE BIT
               MZ   *-29, *-34          * KILL THE ZONE BIT
               MCW  *-41, $-DIV03       * PICK UP ANSWER
               SW   *-44                * SO I CAN PICKUP REMAINDER
               MCW  *-46, $-DIV04       * GET REMAINDER
               CW   *-55                * CLEAR THE WM
               MZ   $-DIV03-1, $-DIV03  * CLEANUP QUOTIENT BITZONE
               MZ   $-DIV04-1, $-DIV04  * CLEANUP REMAINDER BITZONE
               
     * PUSH REMAINDER
               MCW  $-DIV04, 0+X2
               SW   15996+X2
               SBR  X2, 5+X2
               
     * PUSH QUOTIENT
               MCW  $-DIV03, 0+X2
               SW   15996+X2
               SBR  X2, 5+X2

     $-DIV05   B    000                 * JUMP BACK
               
     $-DIV01   DCW  00000               * DIVISOR
     $-DIV02   DCW  00000               * DIVIDEND
     $-DIV03   DCW  00000               * QUOTIENT
     $-DIV04   DCW  00000               * REMAINDER
