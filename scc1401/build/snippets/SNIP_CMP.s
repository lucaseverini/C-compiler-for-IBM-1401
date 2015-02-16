     ****************************************************************
     ** CMP COMPARE FOR INTEGERS (5-digit), CHAR (1-digit) AND ADDRESS (3-digit) 
     ** If OP1 > OP2, RESULT = 1 (1-digit)
     ** If OP2 < OP1, RESULT = 2 (1-digit)
     ** if OP1 == OP2, RESULT = 3 (1-digit)
     ****************************************************************

     $MAIN     SBR  CMP6+3             * Setup return address
     
               POP  OP2
               POP  OP1

               MCW  @3@, RES           * Preset RESULT for EQUAL
               C    OP1, OP2           * Check if operand equal
               BE   CMP5               * Jump over if they are
               B    CHKZI              * Check for negative zeros
               ZA   OP2, TMP           * Move OP2 to TMP
               S    OP1, TMP           * SUBTRACT OP1 FROM TMP
               MN   @1@, TMP           * SET TO 1 SO NEGATIVE IS A j
               BCE  CMP1, TMP, J       * If LSD of TMP is a J, OP1 > OP2
               B    CMP3               * Next check
     CMP1      MCW  @1@, RES           * set RESULT to 1 if op1 > op2
     CMP3      ZA   OP1, TMP           * Move OP2 to TMP
               S    OP2, TMP           * SUBTRACT OP2 FROM TMP
               MN   @1@, TMP           * SET TO 1 SO NEGATIVE IS A j
               BCE  CMP4, TMP, J       * If LSD of TMP is a J, OP1 > OP2 otherwise OP1 == OP2
               B    CMP5               * Go out
     CMP4      MCW  @2@, RES           * Set RESULT to 2 if op2 > op1
     
     CMP5      NOP
               PUSH RES
     
     CMP6      B    000                * Jump back

     CHKZI     SBR  CHKZI4+3           * Setup return address
               C    NEGZI, OP1         * Is OP1 a Negative Zero ?
               BU   CHKZI2             * If is not go next check
               MCW  @?@, OP1           * Set OP1 to Normal Zero
     CHKZI2    C    NEGZI, OP2         * Is OP2 a Negative Zero ?
               BU   CHKZI4             * If is not jump over
               MCW  @?@, OP2           * Set OP2 to Normal Zero
     CHKZI4    B    000                * Jump back

     NEGZI     DCW  -00000             * Negative Zero
     OP1       DCW  00000
     OP2       DCW  00000
     TMP       DCW  00000
     RES       DCW  0

