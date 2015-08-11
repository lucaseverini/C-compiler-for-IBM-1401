     ****************************************************************
     ** CAST NUMBER TO POINTER SNIPPET                             **
     ****************************************************************

     PTRCHR    SBR  X2
     * Casts a 3-digit address to a 1-digit number
     * We will get the last char only and use that
               SW   CAST
               B    0+X2

     ****************************************************************
