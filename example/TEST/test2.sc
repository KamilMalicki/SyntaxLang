.long %liczba1
.long %liczba2
.long %wynik
.string %add_msg "Dodawanie: "
.string %sub_msg "Odejmowanie: "
.string %mul_msg "Mnozenie: "
.string %div_msg "Dzielenie: "
.long %add_msg
.long %sub_msg
.long %mul_msg
.long %div_msg
.byte %temp_char


__start:
    movl $10, %liczba1
    movl $5, %liczba2
    addl %liczba2, %liczba1


    petla_wyswietlaniaa:
    movb %add_msg, %temp_char
    cmpb %temp_char, $0
    je koniec_programua      
    ascii %temp_char
    incl %__index__
    jmp petla_wyswietlaniaa

koniec_programua:
    movl $0, %__index__
    movb $0, %temp_char

    outl %liczba1

    # Test odejmowania
    movl $10, %liczba1
    subl %liczba2, %liczba1

    petla_wyswietlaniab:
    movb %sub_msg, %temp_char
    cmpb %temp_char, $0
    je koniec_programub      
    ascii %temp_char
    incl %__index__
    jmp petla_wyswietlaniab

koniec_programub:
    movl $0, %__index__
    movb $0, %temp_char

    outl %liczba1

    # Test mnozenia
    movl $10, %liczba1
    imull %liczba2, %liczba1


    petla_wyswietlaniac:
    movb %mul_msg, %temp_char
    cmpb %temp_char, $0
    je koniec_programuc      
    ascii %temp_char
    incl %__index__
    jmp petla_wyswietlaniac

koniec_programuc:
    movl $0, %__index__
    movb $0, %temp_char

    outl %liczba1

    # Test dzielenia
    movl $10, %liczba1
    divl %liczba2, %liczba1


    petla_wyswietlaniad:
    movb %div_msg, %temp_char
    cmpb %temp_char, $0
    je koniec_programud      
    ascii %temp_char
    incl %__index__
    jmp petla_wyswietlaniad

koniec_programud:
    movl $0, %__index__
    movb $0, %temp_char

    outl %liczba1

    movl $0, %liczba2
    divl %liczba2, %liczba1

koniec: