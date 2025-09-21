.long %a 
.long %b
.string %msg_equal "a jest rowne b\n"
.string %msg_not_equal "a nie jest rowne b\n"
.string %msg_less "a jest mniejsze od b\n"
.string %msg_greater "a jest wieksze od b\n"
.byte %temp_char

__start:
    movl $10, %a    # Ustaw a na 10
    movl $10, %b    # Ustaw b na 20

    cmpl %a, %b     # Porownaj a z b
    je equal        # Jesli a == b, skocz do 'equal'
    jl less         # Jesli a < b, skocz do 'less'
    jg greater      # Jesli a > b, skocz do 'greater'

equal:
    movl $0, %__index__
    print_equal:
        movb %msg_equal, %temp_char
        cmpb %temp_char, $0
        je exit
        ascii %temp_char
        incl %__index__
        jmp print_equal

less:
    movl $0, %__index__
    print_less:
        movb %msg_less, %temp_char
        cmpb %temp_char, $0
        je exit
        ascii %temp_char
        incl %__index__
        jmp print_less

greater:
    movl $0, %__index__
    print_greater:
        movb %msg_greater, %temp_char
        cmpb %temp_char, $0
        je exit
        ascii %temp_char
        incl %__index__
        jmp print_greater

exit: