.long %val1
.long %val2
.long %val3
.long %wynik1
.long %wynik2
.long %wynik3
.long Wskaznik
.string %a "Wskaznik stosu: "
.string %b "Winki ze stosów: "
.string %c "Ostatni wskaznik stosu: "
.byte %temp_char

__start:
    movl $100, %val1
    movl $200, %val2
    movl $300, %val3

    pushl %val1
    a:
        movb %a, %temp_char
        cmpb %temp_char, $0
        je ak 
        ascii %temp_char
        incl %__index__
        jmp a

    ak:
    outl %__sp__ 
    movb $0, %temp_char
    movl $0, %__index__

    pushl %val2
    b:
        movb %a, %temp_char
        cmpb %temp_char, $0
        je bk 
        ascii %temp_char
        incl %__index__
        jmp b

    bk:

    outl %__sp__
    movb $0, %temp_char
    movl $0, %__index__

    pushl %val3

    c:
        movb %a, %temp_char
        cmpb %temp_char, $0
        je ck 
        ascii %temp_char
        incl %__index__
        jmp c

    ck:

    outl %__sp__

    movb $0, %temp_char
    movl $0, %__index__

    # Zdejmij ze stosu
    popl %wynik3
    popl %wynik2
    popl %wynik1

    d:
        movb %b, %temp_char
        cmpb %temp_char, $0
        je dk 
        ascii %temp_char
        incl %__index__
        jmp d

    dk:
    movb $0, %temp_char
    movl $0, %__index__
   
    outl %wynik1, %wynik2, %wynik3

    e:
        movb %c, %temp_char
        cmpb %temp_char, $0
        je ek 
        ascii %temp_char
        incl %__index__
        jmp e

    ek:

    outl %__sp__

koniec: