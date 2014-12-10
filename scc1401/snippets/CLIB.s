     CLIB      NOP

     OP1       DCW  00000
     OP2       DCW  00000
     RESULT    DCW  00000
     LOP       DCW  00000
     ROP       DCW  00000
     TMP1      DCW  00000
     TMP2      DCW  00000
     TMP3      DCW  00000
     SHIFTS    DCW  00
     SIZE      DCW  00
     IDX       DCW  00

     POWTAB    DCW  00001
               DCW  00002
               DCW  00004
               DCW  00008
               DCW  00016
               DCW  00032
               DCW  00064
               DCW  00128
               DCW  00256
               DCW  00512
               DCW  01024
               DCW  02048
               DCW  04096
               DCW  08192
               DCW  16384
               DCW  32768
 
     ****************************************************************
     ** AND for INTEGER (5 DIGITS)
     ****************************************************************

     ANDI      SBR  ANDI13+3           * Setup return address

               POP  OP2
               POP  OP1

               ZA   OP1, LOP
               ZA   OP2, ROP
               ZA   @0@, RESULT
               ZA   @0@, IDX
               ZA   @16@, SIZE

     ANDI0     C    SIZE, IDX          * Loop...
               BE   ANDI12
               
               B    ADOPI              * Adjust values

               A    RESULT

               ZA   LOP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   ANDI9
               B    ANDI11

     ANDI9     ZA   ROP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   ANDI10
               B    ANDI11
     ANDI10    A    @1@, RESULT
     ANDI11    NOP

               A    LOP
               A    ROP

               A    @1@, IDX

               B    ANDI0

     ANDI12    NOP
               PUSH RESULT

     ANDI13    B    000                * Jump back

     ****************************************************************

     ADOPI     SBR  ADOPI9+3
     
               ZA   LOP, OP1
               ZA   @32767@, OP2
               B    CBIG
               C    RES2, @1@
               BE   ADOPI1   
               B    ADOPI2
     ADOPI1    A    -65536, LOP
               B    ADOPI4

     ADOPI2    ZA   LOP, OP1
               ZA   -32768, OP2
               B    CSMA
               C    RES2, @1@
               BE   ADOPI3
               B    ADOPI4
     ADOPI3    A    @65536@, LOP
     ADOPI4    NOP

               ZA   ROP, OP1
               ZA   @32767@, OP2
               B    CBIG
               C    RES2, @1@
               BE   ADOPI5   
               B    ADOPI6
     ADOPI5    A    -65536, ROP
               B    ADOPI8

     ADOPI6    ZA   ROP, OP1
               ZA   -32768, OP2
               B    CSMA    
               C    RES2, @1@
               BE   ADOPI7
               B    ADOPI8
     ADOPI7    A    @65536@, ROP
     ADOPI8    NOP
     
     ADOPI9    B    000

     ****************************************************************
     ** AND for CHAR (3 DIGITS)
     ****************************************************************

     ANDB      SBR  ANDB13+3           * Setup return address

               POP  OP2
               POP  OP1

               ZA   OP1, LOP
               ZA   OP2, ROP
               ZA   @0@, RESULT
               ZA   @0@, IDX
               ZA   @8@, SIZE

     ANDB0     C    SIZE, IDX          * Loop...
               BE   ANDB12
               
               B    ADOPB              * Adjust values

               A    RESULT

               ZA   LOP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   ANDB9
               B    ANDB11

     ANDB9     ZA   ROP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   ANDB10
               B    ANDB11
     ANDB10    A    @1@, RESULT
     ANDB11    NOP

               A    LOP
               A    ROP

               A    @1@, IDX

               B    ANDB0

     ANDB12    NOP
               PUSH RESULT
               
     ANDB13    B    000                * Jump back

     ****************************************************************

     ADOPB     SBR  ADOPB9+3
     
               ZA   LOP, OP1
               ZA   @127@, OP2
               B    CBIG
               C    RES2, @1@
               BE   ADOPB1   
               B    ADOPB2
     ADOPB1    A    -256, LOP
               B    ADOPB4

     ADOPB2    ZA   LOP, OP1
               ZA   -128, OP2
               B    CSMA
               C    RES2, @1@
               BE   ADOPB3
               B    ADOPB4
     ADOPB3    A    @256@, LOP
     ADOPB4    NOP

               ZA   ROP, OP1
               ZA   @127@, OP2
               B    CBIG
               C    RES2, @1@
               BE   ADOPB5   
               B    ADOPB6
     ADOPB5    A    -256, ROP
               B    ADOPB8

     ADOPB6    ZA   ROP, OP1
               ZA   -128, OP2
               B    CSMA    
               C    RES2, @1@
               BE   ADOPB7
               B    ADOPB8
     ADOPB7    A    @256@, ROP
     ADOPB8    NOP
     
     ADOPB9    B    000

     ****************************************************************
     ** XOR for INTEGER (5 DIGITS)
     ****************************************************************

     XORI      SBR  XORI12+3           * Setup return address

               POP  OP2
               POP  OP1

               ZA   OP1, LOP
               ZA   OP2, ROP

               ZA   @0@, RESULT
               ZA   @0@, IDX
               ZA   @16@, SIZE

     XORI0     C    SIZE, IDX          * Loop...
               BE   XORI11

               B    ADOPI              * Adjust values

               A    RESULT
     
               ZA   LOP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BU   XORI9
     
               ZA   ROP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   XORI10
               A    @1@, RESULT
               B    XORI10
     
     XORI9     ZA   ROP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BU   XORI10
               A    @1@, RESULT
        
     XORI10    A    LOP
               A    ROP
     
               A    @1@, IDX
          
               B    XORI0
     
     XORI11    NOP
               PUSH RESULT
               
     XORI12    B    000                * Jump back

     ****************************************************************
     ** XOR for CHAR (3 DIGITS)
     ****************************************************************

     XORB      SBR  XORB12+3           * Setup return address

               POP  OP2
               POP  OP1

               ZA   OP1, LOP
               ZA   OP2, ROP

               ZA   @0@, RESULT
               ZA   @0@, IDX
               ZA   @8@, SIZE

     XORB0     C    SIZE, IDX          * Loop...
               BE   XORB11

               B    ADOPB              * Adjust values

               A    RESULT
     
               ZA   LOP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BU   XORB9
     
               ZA   ROP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   XORB10
               A    @1@, RESULT
               B    XORB10
     
     XORB9     ZA   ROP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BU   XORB10
               A    @1@, RESULT
        
     XORB10    A    LOP
               A    ROP
     
               A    @1@, IDX
          
               B    XORB0
     
     XORB11    NOP
               PUSH RESULT

     XORB12    B    000                * Jump back

     ****************************************************************
     ** OR for INTEGER (5 DIGITS)
     ****************************************************************
     
     ORI       SBR  ORI13+3            * Setup return address

               POP  OP2
               POP  OP1

               ZA   OP1, LOP
               ZA   OP2, ROP

               ZA   @0@, RESULT
               ZA   @0@, IDX
               ZA   @16@, SIZE
 
     ORI0      C    SIZE, IDX          * Loop...
               BE   ORI12
     
               B    ADOPI              * Adjust values
               
               A    RESULT
     
               ZA   LOP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   ORI9
               B    ORI10
     
     ORI9      A    @1@, RESULT
               B    ORI11
     ORI10     ZA   ROP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   ORI9
     ORI11     NOP
      
               A    LOP
               A    ROP
     
               A    @1@, IDX
           
               B    ORI0
               
     ORI12     NOP
               PUSH RESULT
               
     ORI13     B    000                * Jump back

     ****************************************************************
     ** OR for CHAR (3 DIGITS)
     ****************************************************************
     
     ORB       SBR  ORB13+3            * Setup return address

               POP  OP2
               POP  OP1

               ZA   OP1, LOP
               ZA   OP2, ROP

               ZA   @0@, RESULT
               ZA   @0@, IDX
               ZA   @8@, SIZE
 
     ORB0      C    SIZE, IDX          * Loop...
               BE   ORB12
     
               B    ADOPB              * Adjust values
               
               A    RESULT
     
               ZA   LOP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   ORB9
               B    ORB10
     
     ORB9      A    @1@, RESULT
               B    ORB11
     ORB10     ZA   ROP, OP1
               ZA   @0@, OP2
               B    CSMA
               C    RES2, @1@
               BE   ORB9
     ORB11     NOP
      
               A    LOP
               A    ROP
     
               A    @1@, IDX
           
               B    ORB0

     ORB12     NOP
               PUSH RESULT

     ORB13     B    000                * Jump back

     ****************************************************************
     ** SHIFT LEFT for INTEGER (5 digits)
     ****************************************************************
     
     SHLI      SBR  SHLI11+3

               POP  OP2
               POP  OP1

               ZA   OP1, RESULT
               ZA   OP2, SHIFTS
               ZA   @16@, SIZE
     
               ZA   SHIFTS, OP1
               ZA   @1@, OP2
               B    CSMA
               C    RES2, @1@
               BE   SHLI10

               ZA   SHIFTS, OP1
               ZA   SIZE, OP2
               B    CBIG
               C    RES2, @1@
               BE   SHLI9  
               ZA   SHIFTS, OP1
               ZA   SIZE, OP2
               B    CEQU
               BE   SHLI9  
     
               ZA   @0@, IDX
     SHLI2     C    SHIFTS, IDX          * Loop...
               BE   SHLI10
               
               A    RESULT               * Shift left one position
     
               A    @1@, IDX
     
               B    SHLI2
     
     SHLI9     ZA   @0@, RESULT

     SHLI10    NOP
               PUSH RESULT
               
     SHLI11    B    000

     ****************************************************************
     ** SHIFT LEFT for CHAR (3 digits)
     ****************************************************************
     
     SHLB      SBR  SHLB11+3

               POP  OP2
               POP  OP1

               ZA   OP1, RESULT
               ZA   OP2, SHIFTS
               ZA   @8@, SIZE
     
               ZA   SHIFTS, OP1
               ZA   @1@, OP2
               B    CSMA
               C    RES2, @1@
               BE   SHLB10

               ZA   SHIFTS, OP1
               ZA   SIZE, OP2
               B    CBIG
               C    RES2, @1@
               BE   SHLB9  
               ZA   SHIFTS, OP1
               ZA   SIZE, OP2
               B    CEQU
               BE   SHLB9  
     
               ZA   @0@, IDX
     SHLB2     C    SHIFTS, IDX          * Loop...
               BE   SHLB10
               
               A    RESULT               * Shift left one position
     
               A    @1@, IDX
     
               B    SHLB2
     
     SHLB9     ZA   @0@, RESULT

     SHLB10    NOP
               PUSH RESULT
               
     SHLB11    B    000

     ****************************************************************
     ** SHIFT RIGHT for INTEGER (5 digits)
     ****************************************************************
     
     SHRI      SBR  SHRI11+3

               POP  OP2
               POP  OP1

               ZA   OP1, RESULT
               ZA   OP2, SHIFTS
               ZA   @16@, SIZE
     
               ZA   SHIFTS, OP1
               ZA   @1@, OP2
               B    CSMA
               C    RES2, @1@
               BE   SHRI10

               ZA   SHIFTS, OP1
               ZA   SIZE, OP2
               B    CBIG
               C    RES2, @1@
               BE   SHRI9  
               ZA   SHIFTS, OP1
               ZA   SIZE, OP2
               B    CEQU
               BE   SHRI9  
     
               ZA   @0@, IDX
     SHRI2     C    SHIFTS, IDX          * Loop...
               BE   SHRI10
               
               S    RESULT               * Shift rigt one position
     
               A    @1@, IDX
     
               B    SHRI2
     
     SHRI9     ZA   @0@, RESULT

     SHRI10    NOP
               PUSH RESULT
               
     SHRI11    B    000
     
     ****************************************************************
     ** SHIFT RIGHT for CHAR (3 digits)
     ****************************************************************
     
     SHRB      SBR  SHRB11+3

               POP  OP2
               POP  OP1

               ZA   OP1, RESULT
               ZA   OP2, SHIFTS
               ZA   @8@, SIZE
     
               ZA   SHIFTS, OP1
               ZA   @1@, OP2
               B    CSMA
               C    RES2, @1@
               BE   SHRB10

               ZA   SHIFTS, OP1
               ZA   SIZE, OP2
               B    CBIG
               C    RES2, @1@
               BE   SHRB9  
               ZA   SHIFTS, OP1
               ZA   SIZE, OP2
               B    CEQU
               BE   SHRB9  
     
               ZA   @0@, IDX
     SHRB2     C    SHIFTS, IDX          * Loop...
               BE   SHRB10
               
               S    RESULT               * Shift rigt one position
     
               A    @1@, IDX
     
               B    SHRB2
     
     SHRB9     ZA   @0@, RESULT

     SHRB10    NOP
               PUSH RESULT
               
     SHRB11    B    000
     
     ****************************************************************
     ** PRINT UNSIGNED INTEGER (5 digits) IN OP1
     ****************************************************************
     
     PRTI      SBR  PRTI4+3            * Setup return address
     
               POP  OP1
               
               BWZ  PRTI2, OP1,2       * JUMP IF NO ZONE (OP1 IS POSITIVE, NO CONVERSION)
               BWZ  PRTI1, OP1,B       * JUMP IF B ZONE (OP1 IS POSITIVE, BUT ZONE MUST BE REMOVED)
               MCW  @-@, PRINT         * OP1 IS NEGATIVE, PUT THE MINUS SIGN IN FRONT               
               MZ   OP1-1, OP1              
               MCW  OP1, PRINT+5
               B    PRTI3
     PRTI1     MZ   OP1-1, OP1         * REMOVE ZONE
     PRTI2     MCW  OP1, PRINT+4
     PRTI3     W
               CS   299
     PRTI4     B    000

     ****************************************************************
     ** PRINT SIGNED INTEGER (5 digits) IN OP1 WITH SIGN IF NEGATIVE
     ****************************************************************
     
     PRTIS     SBR  PRTIS4+3           * Setup return address

               POP  OP1

               BWZ  PRTIS2, OP1,2      * JUMP IF NO ZONE (OP1 IS POSITIVE, NO CONVERSION)
               BWZ  PRTIS1, OP1,B      * JUMP IF B ZONE (OP1 IS POSITIVE, BUT ZONE MUST BE REMOVED)
               MCW  @-@, PRINT         * OP1 IS NEGATIVE, PUT THE MINUS SIGN IN FRONT               
               MZ   OP1-1, OP1              
               MCW  OP1, PRINT+5
               B    PRTIS3
     PRTIS1    MZ   OP1-1, OP1         * REMOVE ZONE
     PRTIS2    MCW  OP1, PRINT+4
     PRTIS3    W
               CS   299
     PRTIS4    B    000

     ****************************************************************
     ** PRINT CHAR (3 digits) IN OP1 WITH SIGN IF NEGATIVE
     ****************************************************************
     
     PRTB      SBR  PRTB4+3            * Setup return address
     
               SW   OP1-2              * Set WM to 3rd digit
               BWZ  PRTB2, OP1,2       * JUMP IF NO ZONE (OP1 IS POSITIVE, NO CONVERSION)
               BWZ  PRTB1, OP1,B       * JUMP IF B ZONE (OP1 IS POSITIVE, BUT ZONE MUST BE REMOVED)
               MCW  @-@, PRINT         * OP1 IS NEGATIVE, PUT THE MINUS SIGN IN FRONT 
               MZ   OP1-1, OP1              
               MCW  OP1, PRINT+3
               B    PRTB3
     PRTB1     MZ   OP1-1, OP1         * REMOVE ZONE
     PRTB2     MCW  OP1, PRINT+2
     PRTB3     W
               CS   299
               SW   OP1-4              * Restore WM to 5th digit
                 
     PRTB4     B    000

     ****************************************************************
     ** CMPB COMPARE for CHAR (3 DIGITS)
     ** If OP1 > OP2, RESULT = 1
     ** If OP2 < OP1, RESULT = 2
     ** if OP1 == OP2, RESULT = 3
     ****************************************************************

     CMPB      SBR  CMPB5+3            * Setup return address
     
               POP  OP2
               POP  OP1

               MCW  @00003@, RESULT    * Pre-set RESULT for EQUAL
               B    CHKZI              * Check for negative zeros
               ZA   OP2, TMP3          * Move OP2 to TMP
               S    OP1, TMP3          * SUBTRACT OP1 FROM TMP
               MN   @1@, TMP3          * SET TO 1 SO NEGATIVE IS A j
               BCE  CMPB1, TMP3, J     * If LSD of TMP is a J, OP1 > OP2
               B    CMPB3              * Next check
     CMPB1     MCW  @00001@, RESULT    * set RESULT to 1 if op1 > op2
     CMPB3     ZA   OP1, TMP3          * Move OP2 to TMP
               S    OP2, TMP3          * SUBTRACT OP2 FROM TMP
               MN   @1@, TMP3          * SET TO 1 SO NEGATIVE IS A j
               BCE  CMPB4, TMP3, J     * If LSD of TMP is a J, OP1 > OP2
               B    CMPB5              * Next check
     CMPB4     MCW  @00002@, RESULT    * set RESULT to 2 if op2 > op1
     CMPB5     B    000                * Jump back
  
     ****************************************************************   
     ** CHECK SPECIAL CASE OF NEGATIVE ZEROS AND NORMALIZE THEM  
     ****************************************************************

     NEGZB     DCW  -000               * Negative Zero

     CHKZB     SBR  CHKZB4+3           * Setup return address
               C    NEGZB, OP1         * Is OP1 a Negative Zero ?
               BU   CHKZB2             * If is not go next check
               MCW  @?@, OP1           * Set OP1 to Normal Zero
     CHKZB2    C    NEGZB, OP2         * Is OP2 a Negative Zero ?
               BU   CHKZB4             * If is not jump over
               MCW  @?@, OP2           * Set OP2 to Normal Zero
     CHKZB4    B    000                * Jump back

     ****************************************************************

     NEGZI     DCW  -00000             * Negative Zero

     CHKZI     SBR  CHKZI4+3           * Setup return address
               C    NEGZI, OP1         * Is OP1 a Negative Zero ?
               BU   CHKZI2             * If is not go next check
               MCW  @?@, OP1           * Set OP1 to Normal Zero
     CHKZI2    C    NEGZI, OP2         * Is OP2 a Negative Zero ?
               BU   CHKZI4             * If is not jump over
               MCW  @?@, OP2           * Set OP2 to Normal Zero
     CHKZI4    B    000                * Jump back

     ****************************************************************
     ** CEQU: OP1 == OP2 returns True (1) or False (0)
     ** CBIG: OP1 > OP2 returns True (1) or False (0)
     ** CSMA: OP1 < OP2 returns True (1) or False (0)
     ****************************************************************
  
     RES2      DCW  0                  * Result (True or False)
       
     * OP1 == OP2 ?
     CEQU      SBR  CEQU3+3            * Setup return address
               B    CHKZI              * Check for zeros
               C    OP2, OP1           * Compare operands
               BE   CEQU2              * If equal bail out
               MCW  @0@, RES2          * Set result to False
               B    CEQU3              * If not bail out
     CEQU2     MCW  @1@, RES2          * Set result to True
     CEQU3     B    000                * Jump back

     ****************************************************************

     * OP1 > OP2 ?
     CBIG      SBR  CBIG3+3            * Setup return address
               MCW  @0@, RES2          * Set result to False
               B    CHKZI              * Check for zeros
               C    OP2, OP1           * Compare operands
               BE   CBIG3              * If equal bail out
               S    OP2, OP1           * Subtract operands
               BWZ  CBIG2, OP1, B      * OP1 > OP2 ?
               B    CBIG3              * If not bail out
     CBIG2     MCW  @1@, RES2          * Set result to True
     CBIG3     B    000                * Jump back
     
     ****************************************************************
  
     * OP1 < OP2 ?
     CSMA      SBR  CSMA3+3            * Setup return address
               MCW  @0@, RES2          * Set result to False
               B    CHKZI              * Check for zeros
               C    OP2, OP1           * Compare operands
               BE   CSMA3              * If equal bail out
               S    OP2, OP1           * Subtract operands
               BWZ  CSMA2, OP1, K      * OP1 < OP2 ?
               B    CSMA3              * If not bail out
     CSMA2     MCW  @1@, RES2          * Set result to True
     CSMA3     B    000                * Jump back
