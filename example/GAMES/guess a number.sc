# Program: Zgadnij Liczbę "guess a number.sc"
# Autor: Kamil Malicki
# Opis: Prosta gra, w której gracz próbuje odgadnąć liczbę wylosowaną przez komputer w zadanym zakresie.

.long %attempts
.long %guess
.long %secret_number
.long %min_range
.long %max_range

__start:
    movl $1, %min_range
    movl $100, %max_range
    movl $0, %attempts
    
    rand %min_range, %max_range, %secret_number
    ascii $87, $101, $108, $99, $111, $109, $101, $32, $116, $111, $32, $71, $117, $101, $115, $115, $32, $97, $32, $78, $117, $109, $98, $101, $114, $32, $71, $97, $109, $101, $33
    ascii $10  # new line
    
    ascii $73, $39, $109, $32, $116, $104, $105, $110, $107, $105, $110, $103, $32, $111, $102, $32, $97, $32, $110, $117, $109, $98, $101, $114, $32, $98, $101, $116, $119, $101, $101, $110, $32
    outl %min_range
    ascii $32, $97, $110, $100, $32
    outl %max_range
    ascii $10
    
game_loop:
    incl %attempts
    
    ascii $69, $110, $116, $101, $114, $32, $121, $111, $117, $114, $32, $103, $117, $101, $115, $115, $58, $32
    inl %guess
    
    cmpl %guess, %secret_number
    
    je guessed_correctly
    jl too_low
    jg too_high
    
too_low:
    ascii $84, $111, $111, $32, $108, $111, $119, $33, $32, $84, $114, $121, $32, $97, $103, $97, $105, $110, $46
    ascii $10
    jmp game_loop
    
too_high:
    ascii $84, $111, $111, $32, $104, $105, $103, $104, $33, $32, $84, $114, $121, $32, $97, $103, $97, $105, $110, $46
    ascii $10
    jmp game_loop
    
guessed_correctly:
    ascii $67, $111, $110, $103, $114, $97, $116, $117, $108, $97, $116, $105, $111, $110, $115, $33, $32, $89, $111, $117, $32, $103, $117, $101, $115, $115, $101, $100, $32, $116, $101, $32, $110, $117, $109, $98, $101, $114, $32, $105, $110, $32
    outl %attempts
    ascii $32, $97, $116, $116, $101, $109, $112, $116, $115, $46
    ascii $10
    jmp end_game
    
end_game: