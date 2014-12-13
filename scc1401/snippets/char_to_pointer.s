    $MAIN   SBR  X1
    * Casts a 1-digit char to a 3-digit pointer
            MCW  0+X2,2+X2
			MCW  @00@,1+X2
            SBR  X2,2+X2
            B    0+X1
