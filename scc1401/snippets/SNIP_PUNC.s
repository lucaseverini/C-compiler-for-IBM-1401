     **************************************************************** 
     ** PUNC - Punch and resets punch area and character position
     ****************************************************************
     
     PUNC      SBR  PUNC9+3           * Setup return address     
     
               P                       * Punch card
               MCW  @000@, PUCPOS      * Reset position for next char
     
     PUNC9     B    000                * Jump back
