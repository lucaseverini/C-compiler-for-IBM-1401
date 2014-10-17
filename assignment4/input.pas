{
CONST
    high = 50;

TYPE
    LowNums  = 0..high;
    Giant    = (fee, fie, foe, fum);
    GSet     = SET OF Giant;
    NumSet   = SET OF LowNums;
    SetArray = ARRAY [Giant] OF NumSet;
    Array2d  = ARRAY [LowNums, Giant] OF fie..fum;

    RecordA = RECORD
                  fld1 : NumSet;
                  fld2 : SET OF Giant;
                  fld3 : SET OF 5..12;
                  fld4 : SetArray;
                  fld5 : Array2d;
                  fld6 : Giant;
              END;

    ArrayOfSet = ARRAY [Giant, -5..-1] OF SET OF Giant;

VAR
    gs   : GSet;
    s    : NumSet;
    t    : SET OF (alpha, beta, gamma);

BEGIN
END.
}

BEGIN
    low := 15;
    mid := 45;
    high := 50;

    evens  := [0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20];
    odds   := [1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21];
    primes := [2, 3, 5, 7, 11, 13, 17, 19, 23, 29];
    teens  := [13..19];

    s1 := evens*odds;
    s2 := evens - teens + [high, mid..47, 2*low];
    s3 := evens*primes + teens;
    s4 := s3 - odds;
    s5 := evens + odds + primes + teens;
    s6 := (primes - teens)*odds;

    b1 := odds - primes = [15, 9, 21, 1];
    b2 := teens <= evens + odds;
    b3 := primes >= teens;
    b4 := odds - teens*primes <> [21, 7, 9, 1, 11, 5, 15, 3];
    b5 := 15 IN teens - primes;

    s7 := [];
    i := 1;

    WHILE i <= 50 DO BEGIN
        s7 := s7 + [i];
        i := 2*i;

        IF 8 IN s7 THEN s7 := s7 + [10]
                   ELSE s7 := s7 - [4]
    END
END.
