# Program: test użycia bibioteki "std/main.sc"
# Autor: Kamil Malicki
# Opis: testuje funkcje dołączonej biioteki

.include "std.sc"

.string %hello "Witaj\n\tŚwiecie!\0"

__start:
    call print
