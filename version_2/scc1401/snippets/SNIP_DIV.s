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


               B    *+17

               DCW  @00000@
               DC   @00000000000@

     * PUT DIVIDEND INTO WORKING BL
               ZA   CDIV2,*-7
     * DIVIDE
               D    CDIV1,*-19
     * KILL THE ZONE BIT
               MZ   *-22,*-21
     * KILL THE ZONE BIT
               MZ   *-29,*-34
     * PICK UP ANSWER
               MCW  *-41,CDIV3
     * SO I CAN PICKUP REMAINDER
               SW   *-44
     * GET REMAINDER
               MCW  *-46,CDIV4
     * CLEAR THE WM
               CW   *-55
     * CLEANUP QUOTIENT BITZONE
               MZ   CDIV3-1,CDIV3
     * CLEANUP REMAINDER BITZONE
               MZ   CDIV4-1,CDIV4

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
