    $MAIN   SBR  X1
	* Casts a 3-digit address to a 1-digit number
	* We will get the last char only and use that
            SW   0+X2
            MCW  0+X2,1+X2
            SW   1+X2
            CW   0+X2
            MCW  @000@,0+X2
            MCW  1+X2,15998+X2
            CW   1+X2
            SW   15998+X2
            SBR  X2,15998+X2
            B    0+X1
