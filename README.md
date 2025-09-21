# SyxL — SyntaxLang Interpreter

## Autor
Kamil Malicki

## Co to jest
SyxL to interpreter prostego języka niskopoziomowego wzorowanego na asemblerze.  
Obsługuje typy `.long`, `.byte`, `.float`, `.string`, etykiety, stos wartości, wywołania podprogramów, operacje arytmetyczne i logiczne oraz dyrektywę `.include`.

## Wymagania
- System operacyjny LINUX/DOCKER LINUX CONTAINER
- System plików obsługujący ścieżki (std::filesystem)

## Uruchomienie
```bash
./SyxL <plik.sc>
```

Opcje:
- `-h`, `--help` — wyświetla krótką pomoc

## Składnia źródła
- Komentarze zaczynają się od `#` i są ignorowane.  
- Etykieta kończy się dwukropkiem `label:` i może być celem skoków.  
- Białe znaki i puste linie są ignorowane.  
- Nazwy zmiennych w dyrektywach muszą zaczynać się od `%`, np. `.long %counter`.  
- Główny punkt startu programu musi mieć etykietę `__start:`. Interpreter zgłosi błąd jeśli jej brak.

## Dyrektywy
- `.long %name` — deklaruje zmienną całkowitą 32-bitową o nazwie `name`. Wartość początkowa 0.
- `.byte %name` — deklaruje zmienną 8-bitową (unsigned char). Wartość początkowa 0.
- `.float %name` — deklaruje zmienną float. Wartość początkowa 0.0.
- `.string %name "tekst"` — deklaruje zmienną tekstową. Zawartość między cudzysłowami.
- `.include "plik.sc"` — włącza plik. Ścieżki są rozwiązywane względem katalogu pliku, który zawiera dyrektywę. Pliki dodawane są tylko raz, aby zapobiec cyklicznym include'om.

## Zmienne specjalne
- `%__index__` — indeks używany przy operacjach na stringach. Inicjalnie `0`. Może być ustawiany przez instrukcje jak `movl`, `movb`, operacje arytmetyczne itp.
- `%__sp__` — wskaźnik stosu wartości pomocniczych. Inicjalnie `0`. Po operacjach `push*` ustawiany na indeks ostatniego elementu. Po opróżnieniu stosu ustawiany na `-1`.

## Model pamięci
- `.long` -> map<string,int> (int).
- `.byte` -> map<string,unsigned char>.
- `.float` -> map<string,float>.
- `.string` -> map<string,string>.
- Dodatkowy stos wartości to `std::vector<float> value_stack_vector`. Wszystkie `push*` zapisują wartość jako float. `pop*` konwertują typy z powrotem.

## Flagi porównań
- `cmpl` / `cmpb` / `cmpf` ustawiają trzy flagi: `zero_flag`, `less_flag`, `greater_flag`. Kolejność argumentów ma znaczenie. Porównanie to: porównaj `arg1` do `arg2`.
  - `zero_flag` gdy `arg1 == arg2`.
  - `less_flag` gdy `arg1 < arg2`.
  - `greater_flag` gdy `arg1 > arg2`.

## Błędy wykonania (najczęstsze)
- Brak etykiety `__start` -> program się nie uruchomi.  
- Skok do nieistniejącej etykiety -> błąd.  
- Dzielenie/modulo przez zero -> `Runtime Error`.  
- `pop*` z pustego stosu -> `Runtime Error`.  
- Ustawienie niezadeklarowanej zmiennej -> `Runtime Error`.  
- Nieznana instrukcja lub błąd składni -> `Runtime Error`.

---
# Instrukcje — szczegóły i przykłady

Dla instrukcji ogólny format to: `instr arg1, arg2` lub `instr arg1 arg2` w zależności od instrukcji. Interpreter dopuszcza nagromadzenie wielu operandów tam, gdzie to sensowne (np. `ascii a b c`). W przykładach używam `%var` dla zmiennych, `$n` dla natychmiastowych (immediate) i `label:` dla etykiet.

## Przenoszenie danych
- `movl src, dst` — przenosi wartość całkowitą.  
  - `src` może być `$10`, `%var_long`, `%var_byte` lub `%str` (w przypadku stringa odczyt zostanie pobrany jako ASCII znak o indeksie `%__index__`).  
  - `dst` musi być `%var_long` lub specjalne `%__index__` / `%__sp__`.  
  - Przykład: `movl $5, %counter` | `movl %a, %b` | `movl %msg, %tmp` (odczyt znaku pod `%__index__`).
- `movb src, dst` — jak `movl` ale 8-bit. Konwersje zachodzą przez rzutowanie do unsigned char.  
  - Przykład: `movb $65, %c` -> zapisze 65 do `%c`. `movb %msg, %tmp` -> odczyta znak z `%msg[%__index__]`.
- `movf src, dst` — przenosi wartość float. `src` może być `$1.5` lub `%floatvar`.
  - Przykład: `movf $3.14, %pi`.

## Wyjście i wejście
- `ascii var1 var2 ...` — dla każdego argumentu wywołuje `(char)get_value(arg)` i wypisuje znak bez odstępu. Przydatne do drukowania pojedynczych znaków.  
  - Przykład: `ascii %tmp` gdzie `%tmp` zawiera kod ASCII.
- `outl a b ...` — wypisuje wartości całkowite oddzielone spacjami i kończy linią.  
- `outb a b ...` — wypisuje wartości bajtowe (jako int).  
- `outf a b ...` — wypisuje wartości float.  
- `inl %var` — czyta liczbę całkowitą z wejścia i zapisuje do `%var` lub `%__index__` / `%__sp__`.  
- `inb %var` — czyta liczbę i zapisuje do zmiennej bajtowej.  
- `inf %var` — czyta float i zapisuje do zmiennej float.

## Arytmetyka i uszkodzenia typu
- `addl src, dst` / `addb` / `addf` — `dst = dst + src`. `dst` może być specjalnym `%__index__` lub `%__sp__`.  
  - Przykład: `addl $1, %counter`.
- `subl`, `subb`, `subf` — odejmowanie.
- `imull`, `imulb`, `mulf` — mnożenie.
- `divl`, `divb`, `divf` — dzielenie. Dzielenie przez 0 powoduje `Runtime Error`.
- `modl`, `modb` — modulo. Dla modulo przez 0 rzucany jest `Runtime Error`.
- Uwaga na `byte`: operacje na `.byte` są wykonywane na typie `unsigned char` i obcięte do 8 bitów.

## Porównania i skoki warunkowe
- `cmpl a, b` / `cmpb` / `cmpf` — porównuje `a` z `b`. Ustawia flagi. Kolejność argumentów istotna.  
- `jmp label` — bezwarunkowy skok.  
- `je label` — skok gdy `zero_flag == true`.  
- `jne label` — skok gdy `zero_flag == false`.  
- `jl label` — skok gdy `less_flag == true` (czyli `a < b`).  
- `jg label` — skok gdy `greater_flag == true` (czyli `a > b`).

## Logika bitowa
- `andl`, `andb` — bitowe AND.  
- `orl`, `orb` — bitowe OR.  
- `xorl`, `xorb` — bitowe XOR.  
- `notl`, `notb` — bitowa negacja (~).  
- `nandl`, `nandb`, `norl`, `norb`, `xnorl`, `xnorb` — kombinacje negacji i operacji logicznych zgodnie z nazwą.

## Operacje stosu (wartości pomocnicze)
- `pushl src` / `pushb src` / `pushf src` — popytuje `src` na wewnętrzny stos `value_stack_vector` (zapis jako float). Po `push` `%__sp__` = indeks ostatniego elementu `(size-1)`.  
- `popl dst` / `popb dst` / `popf dst` — usuwa ostatni element ze stosu i zapisuje skonwertowaną wartość do `dst`. Jeśli stos jest pusty -> `Runtime Error`.
  - Uwaga: stos jest odrębny od zmiennych `.long/.byte/.float`. Jest to pomocniczy stos wartości używany np. do przekazywania wyników i tymczasów.

## Podprogramy
- `call label` — zapisuje adres powrotu i ustawia `program_counter` na `label`.  
- `ret` — odczytuje adres powrotu i skacze do niego. `ret` bez adresu powrotu powoduje błąd.

## Zamiany i konwersje
- `xchgl a, b` / `xchgb` / `xchgf` — wymienia zawartość dwóch zmiennych tego samego typu.  
- `cvl src, dst` — konwertuje bajt (lub inne) do long (int).  
- `cvb src, dst` — konwertuje długą wartość do bajtu.  
- `cvf src, dst` — konwertuje wartość całkowitą do float.

## Przesunięcia i rotacje
- `shll src, dst` / `shlb` — logiczne przesunięcie w lewo (`dst <<= src`).  
- `shrl src, dst` / `shrb` — logiczne przesunięcie w prawo (`dst >>= src`).  
- `roll src, dst` / `rolb` — rotacja bitowa w lewo.  
- `rorl src, dst` / `rorb` — rotacja bitowa w prawo.  
Uwaga: unikaj przesunięć o liczbę bitów >= rozmiaru typu. Interpreter nie maskuje liczby przesunięć i operacje takie mogą dać nieprzewidywalne rezultaty albo powodować UB w C++.

## Losowość
- `rand min max dst` — generuje losową liczbę całkowitą z zakresu `[min, max]` i zapisuje do `dst` (może być `%__index__`, `%__sp__`, `%long` lub `%byte`).  
- `randf min max dst` — generuje float z rozkładu jednostajnego `[min, max]` i zapisuje do zmiennej float.

## Operacje na stringach
- `lodsb %str, %dst_byte` — pobiera znak z `%str` pod indeksem `%__index__` i zapisuje go do `%dst_byte` (typ `.byte`).  
- `movsb src, %str` — zapisuje wartość bajtową `src` do `%str[%__index__]`.  
- `stosb src, %str` — to samo co `movsb` (alias).  
Uwaga: indeks musi być w granicach 0..length-1. Próba zapisu poza granicami zgłosi `Runtime Error`.

---
# Przykłady

### Hello World (poprawione)
```asm
.string %msg "Hello World!\n"
.byte %tmp
.long %len

__start:
    movl $0, %__index__
    movl $13, %len      # długość "Hello World!\n" = 13
loop:
    cmpl %__index__, %len
    je end
    lodsb %msg, %tmp
    ascii %tmp
    incl %__index__
    jmp loop
end:
```

### Prosty program porównujący liczby
```asm
.long %n
.long %i
.long %sum

__start:
    movl $5, %n
    movl $0, %i
    movl $0, %sum
loop:
    cmpl %i, %n
    je done
    addl %i, %sum
    incl %i
    jmp loop
done:
    outl %sum
```

---
# Wskazówki praktyczne
- Zawsze zadeklaruj wszystkie zmienne przed ich użyciem. Interpreter nie tworzy zmiennych „w locie”.  
- Przy operacjach na stringach najpierw ustaw `%__index__`. Używaj `.byte` jako tymczasowego bufora dla instrukcji `lodsb`.  
- Unikaj przesunięć o >= liczba bitów typu.  
- Sprawdź obecność etykiety `__start:` przed uruchomieniem.

---
## Repozytorium
https://github.com/KamilMalicki/SyntaxLang/

