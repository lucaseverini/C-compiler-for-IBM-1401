    ****************************************************************
    ** DIVISION SNIPPET                                           **
    ****************************************************************
     * SETUP RETURN ADDRESS
      DIV      SBR  DIVEND+3

     * POP DIVIDEND
               MCW  0+X2,CDIV2
               SBR  X2,15995+X2

     * POP DIVISOR
               MCW  0+X2,CDIV1
               SBR  X2,15995+X2


               B    *+17               * Branch 17 places down?

               DCW  @00000@
               DC   @00000000000@

               ZA   CDIV2,*-7        * PUT DIVIDEND INTO WORKING BL
               D    CDIV1,*-19       * DIVIDE
               MZ   *-22,*-21        * KILL THE ZONE BIT
               MZ   *-29,*-34        * KILL THE ZONE BIT
               MCW  *-41,CDIV3       * PICK UP ANSWER
               SW   *-44              * SO I CAN PICKUP REMAINDER
               MCW  *-46,CDIV4       * GET REMAINDER
               CW   *-55              * CLEAR THE WM
               MZ   CDIV3-1,CDIV3    * CLEANUP QUOTIENT BITZONE
               MZ   CDIV4-1,CDIV4    * CLEANUP REMAINDER BITZONE

     * PUSH REMAINDER
               SBR  X2,5+X2
               SW   15996+X2
               MCW  CDIV4,0+X2

     * PUSH QUOTIENT
               SBR  X2,5+X2
               SW   15996+X2
               MCW  CDIV3,0+X2

     * JUMP BACK
     DIVEND    B    000

     * DIVISOR
     CDIV1     DCW  00000
     * DIVIDEND
     CDIV2     DCW  00000
     * QUOTIENT
     CDIV3     DCW  00000
     * REMAINDER
     CDIV4     DCW  00000
