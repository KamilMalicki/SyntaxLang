.long %liczba 
.long %maska 
.long %rezultat
.byte %bajtl

__start:
    movl $255, %liczba
    cvb %liczba, %bajtl
    outb %bajtl

    movl $5, %liczba        
    movl $12, %maska        
    andl %maska, %liczba    
    outl %liczba

    movl $1, %liczba        
    shll $2, %liczba       
    outl %liczba

koniec: