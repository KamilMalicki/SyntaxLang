.string %message "Hello, world!"
.byte %byte_val

__start:
    movl $6, %__index__
    movsb $87, %message
    incl %__index__
    lodsb %message, %byte_val
    movsb %byte_val, %message
    movl $0, %__index__
    print_loop:
        movb %message, %byte_val
        ascii %byte_val
        incb %__index__
        cmpl %__index__, $13
        jne print_loop