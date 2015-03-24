     CLNNUM    SBR  X1
     * Normalizes the zone bits of a number, leaving either A=0B=0
     * for a positive or A=0B=1 for a negative
     * Do nothing on either no zone bits or only a b zone bit
               BWZ  CLNUME,0+X2,2
               BWZ  CLNUME,0+X2,K
     * else clear the zone bits, as it is positive
               MZ   @ @,0+X2
     CLNUME    B    0+X1
