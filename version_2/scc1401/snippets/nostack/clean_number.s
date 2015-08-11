     ****************************************************************
     ** CLEAN NUMBER SNIPPET                                       **
     ****************************************************************

     * Normalizes the zone bits of a number, leaving either A=0B=0
     * for a positive or A=0B=1 for a negative
     CLNNMN    SBR  X2
     * Do nothing on either no zone bits or only b zone bit
               BWZ  CLNNME,CAST,2
               BWZ  CLNNME,CAST,K
     * else clear the zone bits, as it is positive
               MZ   @ @,CAST
     CLNNME    B    0+X2

     ****************************************************************
