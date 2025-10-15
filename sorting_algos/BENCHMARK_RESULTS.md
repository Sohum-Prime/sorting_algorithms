# Sorting Algorithm Benchmark Results

## Size: 10

| Algorithm | Random | Sorted | Reverse | Nearly Sorted |
|-----------|------|------|------|------|
| Heap Sort | 8 µs | 22 µs | 9 µs | 9 µs |
| Insertion Sort | 3 µs | 13 µs | 6 µs | 3 µs |
| Merge Sort | 13 µs | 34 µs | 22 µs | 21 µs |
| Quick Sort | 41 µs | 39 µs | 27 µs | 26 µs |
| Selection Sort | 5 µs | 10 µs | 6 µs | 5 µs |

## Size: 100

| Algorithm | Random | Sorted | Reverse | Nearly Sorted |
|-----------|------|------|------|------|
| Heap Sort | 38 µs | 39 µs | 35 µs | 37 µs |
| Insertion Sort | 69 µs | 257 µs | 428 µs | 11 µs |
| Merge Sort | 52 µs | 257 µs | 39 µs | 39 µs |
| Quick Sort | 55 µs | 177 µs | 66 µs | 83 µs |
| Selection Sort | 341 µs | 347 µs | 331 µs | 339 µs |

## Size: 1000

| Algorithm | Random | Sorted | Reverse | Nearly Sorted |
|-----------|------|------|------|------|
| Heap Sort | 227 µs | 233 µs | 203 µs | 209 µs |
| Insertion Sort | 85 µs | 7 ms | 652 µs | 3 µs |
| Merge Sort | 485 µs | 614 µs | 421 µs | 412 µs |
| Quick Sort | 541 µs | 741 µs | 439 µs | 513 µs |
| Selection Sort | 922 µs | 4 ms | 862 µs | 857 µs |

## Size: 10000

| Algorithm | Random | Sorted | Reverse | Nearly Sorted |
|-----------|------|------|------|------|
| Heap Sort | 1 ms | 2 ms | 1 ms | 1 ms |
| Insertion Sort | 6 ms | 32 ms | 59 ms | 27 µs |
| Merge Sort | 1 ms | 2 ms | 760 µs | 1 ms |
| Quick Sort | 3 ms | 4 ms | 1 ms | 1 ms |
| Selection Sort | 39 ms | 44 ms | 39 ms | 40 ms |

## Size: 50000

| Algorithm | Random | Sorted | Reverse | Nearly Sorted |
|-----------|------|------|------|------|
| Heap Sort | 7 ms | 7 ms | 4 ms | 4 ms |
| Insertion Sort | 171 ms | 786 ms | 1.441 s | 111 µs |
| Merge Sort | 6 ms | 12 ms | 4 ms | 4 ms |
| Quick Sort | 12 ms | 20 ms | 8 ms | 8 ms |
| Selection Sort | 1.026 s | 1.025 s | 998 ms | 1.010 s |

