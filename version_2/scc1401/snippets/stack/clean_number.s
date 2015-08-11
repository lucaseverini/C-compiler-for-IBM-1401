     ****************************************************************
     ** CLEAN NUMBER SNIPPET                                       **
     ****************************************************************

     * Normalizes the zone bits of a number, leaving either A=0B=0
     * for a positive or A=0B=1 for a negative
     CLNNMN    SBR  X1
     * Do nothing on either no zone bits or only b zone bit
               BWZ  CLNNME,0+X2,2
               BWZ  CLNNME,0+X2,K
     * else clear the zone bits, as it is positive
               MZ   @ @,0+X2
     CLNNME    B    0+X1

     ****************************************************************
