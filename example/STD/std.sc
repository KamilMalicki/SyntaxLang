# Program: test użycia bibioteki "std/std.sc"
# Autor: Kamil Malicki
# Opis: bibioteka z funkcją 

.byte %ch

print:
    movl $0, %__index__

.ps_loop:
    lodsb %hello, %ch
    cmpb %ch, $92          
    jne .normal_char

    incl %__index__
    lodsb %hello, %ch
    cmpb %ch, $48         
    je .ps_end             

    cmpb %ch, $110         
    je .newline
    cmpb %ch, $116         
    je .tab

    ascii %ch
    incl %__index__
    jmp .ps_loop

.newline:
    ascii $10
    incl %__index__
    jmp .ps_loop

.tab:
    ascii $9
    incl %__index__
    jmp .ps_loop

.normal_char:
    ascii %ch
    incl %__index__
    jmp .ps_loop

.ps_end:
    ret
