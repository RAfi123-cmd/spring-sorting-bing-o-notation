# Sorting Algorithm & Big O Notation

REST API berbasis **Java Spring Boot** yang mendemonstrasikan **6 algoritma
sorting klasik** beserta analisis **Big O Notation**-nya secara empiris —
setiap request sorting mengembalikan jumlah perbandingan, penulisan, kedalaman
rekursi, dan penggunaan memori tambahan yang **benar-benar terukur** saat
kode dijalankan, bukan sekadar label teori.

Studi kasus menggunakan dataset nyata dari Kaggle: **"E-Commerce Data"
(Online Retail)** — `data.csv` (±540.000 baris transaksi retail UK).

---

## Daftar Isi

- [Konsep Big O Notation](#konsep-big-o-notation)
- [Algoritma yang Tersedia](#algoritma-yang-tersedia)
- [Fitur](#fitur)
- [Tech Stack](#tech-stack)
- [Struktur Project](#struktur-project)
- [Konsep OOP yang Diterapkan](#konsep-oop-yang-diterapkan)
- [Setup & Menjalankan](#setup--menjalankan)
- [Dokumentasi API](#dokumentasi-api)
- [Catatan Penting / Known Issues](#catatan-penting--known-issues)

---

## Konsep Big O Notation

Big O Notation menggambarkan bagaimana **waktu eksekusi (Time Complexity)**
atau **kebutuhan memori tambahan (Space Complexity)** suatu algoritma
bertumbuh seiring bertambahnya ukuran input `n`.

| Notasi | Nama | Contoh |
|---|---|---|
| O(1) | Constant | Mengakses elemen array lewat index |
| O(log n) | Logarithmic | Binary search; kedalaman rekursi Merge/Quick/Heap Sort |
| O(n) | Linear | Menelusuri seluruh elemen satu kali |
| O(n log n) | Linearithmic | Merge Sort, Quick Sort (rata-rata), Heap Sort |
| O(n²) | Quadratic | Bubble Sort, Selection Sort, Insertion Sort (worst case) |

Aplikasi ini mengukur dua sisi Big O secara langsung lewat kelas
[`SortMetrics`](src/main/java/com/example/sorting_app/sorting/SortMetrics.java):

- **Time Complexity** → `comparisons`, `writes`, `elapsedMillis`
- **Space Complexity** → `peakRecursionDepth` (kedalaman call stack rekursif),
  `peakAuxiliaryArrayElements` (jumlah elemen di array/list sementara)

## Algoritma yang Tersedia

| Key | Algoritma | Time Complexity | Space Complexity | In-place? |
|---|---|---|---|---|
| `bubble` | Bubble Sort | Best O(n) &#124; Avg/Worst O(n²) | O(1) | Ya |
| `selection` | Selection Sort | O(n²) | O(1) | Ya |
| `insertion` | Insertion Sort | Best O(n) &#124; Avg/Worst O(n²) | O(1) | Ya |
| `merge` | Merge Sort | O(n log n) | O(n) | Tidak |
| `quick` | Quick Sort | Best/Avg O(n log n) &#124; Worst O(n²) | Avg O(log n) &#124; Worst O(n) | Ya |
| `heap` | Heap Sort | O(n log n) | O(1) teori / O(log n) stack (implementasi rekursif) | Ya |

### Kelebihan & Kekurangan

<details>
<summary><b>Bubble Sort</b></summary>

**Kelebihan:** sederhana, in-place (O(1)), stabil, ada early-exit sehingga
best-case O(n) pada data yang sudah terurut.
**Kekurangan:** sangat lambat untuk data besar (O(n²)), jumlah writes tinggi,
tidak praktis untuk dataset besar.
</details>

<details>
<summary><b>Selection Sort</b></summary>

**Kelebihan:** jumlah writes paling minimal di antara algoritma O(n²)
(maksimal n-1 kali), in-place, performa tidak dipengaruhi urutan data awal.
**Kekurangan:** tetap O(n²) di semua kasus (tidak ada best-case O(n)), tidak
stabil secara default.
</details>

<details>
<summary><b>Insertion Sort</b></summary>

**Kelebihan:** sangat efisien untuk data yang sudah hampir terurut (mendekati
O(n)), in-place, stabil, bersifat online (bisa mengurutkan data streaming).
**Kekurangan:** tetap O(n²) pada data acak/terbalik, kurang efisien untuk
dataset besar.
</details>

<details>
<summary><b>Merge Sort</b></summary>

**Kelebihan:** performa O(n log n) konsisten di semua kasus, stabil, cocok
untuk data sangat besar/external sorting, mudah diparalelkan.
**Kekurangan:** butuh memori tambahan O(n) (tidak in-place), overhead lebih
besar dari Insertion Sort untuk data kecil.
</details>

<details>
<summary><b>Quick Sort</b></summary>

**Kelebihan:** rata-rata tercepat secara praktis untuk data acak, tidak butuh
array tambahan (hanya call-stack).
**Kekurangan:** worst-case O(n²) pada data yang sudah terurut atau banyak
duplikat (pivot elemen terakhir rentan partisi tidak seimbang), tidak stabil.
</details>

<details>
<summary><b>Heap Sort</b></summary>

**Kelebihan:** O(n log n) terjamin di semua kasus, in-place, tidak terpengaruh
duplikat/urutan awal data seperti Quick Sort.
**Kekurangan:** tidak stabil, secara praktik sedikit lebih lambat dari Quick
Sort karena pola akses memori heap kurang cache-friendly.
</details>

## Fitur

- **CRUD Transaksi** — create, read (dengan pagination), update, delete data
  transaksi di PostgreSQL.
- **Import CSV** — upload dataset Kaggle langsung lewat endpoint, di-batch
  insert (500 baris/batch) agar tetap cepat untuk ±540.000 baris.
- **Sort satu algoritma** — jalankan salah satu dari 6 algoritma pada data
  nyata dari database, dapatkan hasil terurut + metrik pengukurannya.
- **Benchmark multi-algoritma & multi-ukuran** — bandingkan beberapa algoritma
  sekaligus di beberapa ukuran data dalam satu request, siap untuk analisis
  Big O.
- **Guard O(n²)** — algoritma kuadratik otomatis diblokir di atas batas aman
  (`QUADRATIC_SAFE_LIMIT`) kecuali `force=true` disertakan, supaya server
  tidak macet karena request yang tidak sengaja berat.

## Tech Stack

- **Java 25**, **Spring Boot 4.1.1** (`spring-boot-starter-webmvc`,
  `spring-boot-starter-data-jpa`, `spring-boot-starter-actuator`)
- **PostgreSQL** — database `sorting_db`
- **Apache Commons CSV** — parsing file CSV Kaggle
- **Lombok** — mengurangi boilerplate (constructor, getter/setter di sebagian
  kelas)
- **Maven** (`mvnw`)

## Struktur Project

```
src/main/java/com/example/sorting_app/
├── SortingAppApplication.java
├── constant/
│   ├── AppConstant.java            # default value, limit, pola tanggal CSV
│   ├── SortAlgorithmKeys.java      # key algoritma: bubble, quick, dst.
│   ├── SortableFields.java         # nama field yang bisa dijadikan sort key
│   ├── SortingConstant.java        # path mapping SortingController
│   └── TransactionConstant.java    # path mapping TransactionController
├── controller/
│   ├── SortingController.java      # /api/algorithms, /api/sort, /api/benchmark
│   └── TransactionController.java  # /api/transactions/**
├── dto/
│   ├── SortResponse.java
│   └── BenchmarkResult.java
├── persitance/
│   ├── entity/Transaction.java
│   ├── repository/TransactionRepository.java
│   └── service/
│       ├── CsvImportService.java   # parsing & batch import CSV Kaggle
│       └── TransactionService.java # CRUD
└── sorting/
    ├── SortAlgorithm.java          # kontrak setiap algoritma (Abstraction)
    ├── AbstractSortAlgorithm.java  # Template Method: timer + hook doSort()
    ├── SortAlgorithmRegistry.java  # lookup algoritma by key (Polymorphism)
    ├── SortMetrics.java            # instrumentasi comparisons/writes/depth/aux
    ├── TransactionFieldComparators.java
    └── impl/
        ├── BubbleSortAlgorithm.java
        ├── SelectionSortAlgorithm.java
        ├── InsertionSortAlgorithm.java
        ├── MergeSortAlgorithm.java
        ├── QuickSortAlgorithm.java
        └── HeapSortAlgorithm.java
```

## Konsep OOP yang Diterapkan

| Pilar | Penerapan |
|---|---|
| **Encapsulation** | Field pada `Transaction` dan `SortMetrics` bersifat `private`, diakses lewat getter/setter atau method terkontrol (`incrementComparisons()`, dll). |
| **Abstraction** | Interface `SortAlgorithm` menyembunyikan detail implementasi; controller hanya bergantung pada kontrak ini. |
| **Inheritance** | `AbstractSortAlgorithm` mewariskan alur pengukuran waktu (`sortAndMeasure`) ke 6 subclass algoritma konkret. |
| **Polymorphism** | `SortAlgorithmRegistry` menyimpan `List<SortAlgorithm>`; perilaku yang dijalankan saat `sortAndMeasure()` dipanggil bergantung objek konkret yang dipilih saat runtime (`bubble`, `quick`, dst). |

**Template Method Pattern** — `AbstractSortAlgorithm.sortAndMeasure()`
mengatur alur tetap (mulai timer → panggil `doSort()` → catat waktu ke
`SortMetrics`), sehingga tiap algoritma konkret cukup mengimplementasikan
`doSort()` dengan logika sorting yang berbeda-beda, tanpa mengurus boilerplate
pengukuran waktu sendiri-sendiri.

## Setup & Menjalankan

### 1. Siapkan Database

Buat database PostgreSQL bernama `sorting_db`, lalu sesuaikan kredensial di
`src/main/resources/application.properties` bila perlu:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sorting_db
spring.datasource.username=postgres
spring.datasource.password=123456
```

Skema tabel `transactions` dibuat otomatis oleh Hibernate
(`spring.jpa.hibernate.ddl-auto=update`) saat aplikasi pertama kali jalan.

### 2. Jalankan Aplikasi

```bash
./mvnw spring-boot:run
```

Aplikasi berjalan di `http://localhost:8080`.

### 3. Import Dataset

Upload `data.csv` (sudah ada di root project ini) lewat Postman atau curl:

```bash
curl -X POST http://localhost:8080/api/transactions/import \
  -F "file=@data.csv"
```

## Dokumentasi API

### Transaction — CRUD (`/api/transactions`)

| Method | Path | Keterangan |
|---|---|---|
| GET | `/all?page=0&size=20&sortBy=id&direction=asc` | List transaksi (paginated) |
| GET | `/{id}` | Detail satu transaksi |
| POST | `/create` | Buat transaksi baru (body JSON) |
| PUT | `/edit/{id}` | Update transaksi |
| DELETE | `/delete/{id}` | Hapus transaksi |
| GET | `/count` | Total jumlah transaksi |
| POST | `/import` | Upload CSV (`multipart/form-data`, key `file`) |

### Sorting & Benchmark (`/api`)

#### `GET /api/algorithms`
Daftar semua algoritma beserta kompleksitasnya.

```json
[
  {
    "key": "bubble",
    "name": "Bubble Sort",
    "timeComplexity": "Best: O(n) | Average/Worst: O(n^2)",
    "spaceComplexity": "O(1)"
  }
]
```

#### `GET /api/sort`

| Parameter | Default | Keterangan |
|---|---|---|
| `algorithm` | *(wajib)* | `bubble`, `selection`, `insertion`, `merge`, `quick`, `heap` |
| `field` | — | **isi manual, lihat catatan di bawah** |
| `order` | `asc` | `asc` atau `desc` |
| `limit` | `1000` | jumlah baris yang diambil dari database |
| `force` | `false` | paksa jalankan algoritma O(n²) di atas batas aman |

Field yang valid untuk `field` (tidak case-sensitive):
`quantity`, `unitPrice`, `customerId`, `invoiceDate`, `description`,
`stockCode`, `invoiceNo`, `country`.

Contoh:

```
GET /api/sort?algorithm=quick&field=unitPrice&order=asc&limit=1000
```

```json
{
  "algorithm": "Quick Sort",
  "timeComplexity": "Best/Average: O(n log n) | Worst: O(n^2)",
  "spaceComplexity": "Average: O(log n) | Worst: O(n)",
  "dataSize": 1000,
  "comparisons": 33952,
  "writes": 31536,
  "peakRecursionDepth": 108,
  "peakAuxiliaryArrayElements": 0,
  "elapsedMillis": "6.6410 ms",
  "data": [ ... ]
}
```

> `elapsedMillis` otomatis diformat ke satuan yang sesuai (µs / ms / s)
> tergantung besar-kecilnya waktu eksekusi.

#### `GET /api/benchmark`

| Parameter | Default | Keterangan |
|---|---|---|
| `algorithms` | semua algoritma | daftar key dipisah koma, mis. `bubble,quick` |
| `field` | — | **isi manual, lihat catatan di bawah** |
| `order` | `asc` | `asc` atau `desc` |
| `sizes` | — | **wajib diisi manual**, mis. `100,1000,5000` |
| `force` | `false` | paksa jalankan algoritma O(n²) di atas batas aman |

Contoh:

```
GET /api/benchmark?algorithms=bubble,quick,merge&field=unitPrice&sizes=100,1000,5000&force=true
```

Setiap kombinasi algoritma × ukuran menghasilkan satu objek hasil, siap
dipindah ke chart untuk melihat kurva pertumbuhan O(n) vs O(n log n) vs O(n²).

## Catatan Penting / Known Issues

Dua hal berikut ditemukan pada konfigurasi default saat ini — **sebaiknya
selalu isi parameter terkait secara eksplisit** di setiap request agar tidak
menemui error:

1. **`field` tidak boleh dikosongkan.** Konstanta `SortableFields.UNIT_PRICE`
   saat ini bernilai `"unit_price"` (dengan underscore), sedangkan peta
   comparator di `TransactionFieldComparators` hanya mengenali key tanpa
   underscore (`"unitprice"`). Akibatnya, jika parameter `field` tidak
   disertakan di request, aplikasi akan melempar
   `NoSuchElementException: Unknown sort field 'unit_price'`. **Selalu kirim
   `field=unitPrice`** (atau salah satu nama valid lain) secara eksplisit.
2. **`sizes` wajib diisi di `/api/benchmark`.** Nilai default
   `AppConstant.DEFAULT_BENCMARK` saat ini adalah string kosong (`""`), yang
   akan menyebabkan `NumberFormatException` saat parameter `sizes` tidak
   disertakan. **Selalu kirim `sizes=...` secara eksplisit**, misalnya
   `sizes=100,1000,5000`.

Kedua hal ini murni soal *default value* pada konstanta, bukan pada logika
algoritma sorting-nya — begitu parameter diisi manual, seluruh endpoint
bekerja normal seperti pada contoh-contoh di atas.

## Rekomendasi Link Pembelajaran

Untuk memperdalam pemahaman mengenai **Sorting Algorithms** dan **Big O Notation**, berikut beberapa sumber pembelajaran yang dapat digunakan.

### Sorting Algorithms — Java

- **[GeeksforGeeks — Sorting Algorithms in Java](https://www.geeksforgeeks.org/java/sorting-algorithms-in-java/)**  
  Penjelasan berbagai algoritma sorting menggunakan Java, termasuk **Bubble Sort, Selection Sort, Insertion Sort, Merge Sort, Quick Sort,** dan **Heap Sort**. Cocok sebagai referensi implementasi algoritma pada project Java Spring Boot.

- **[Baeldung — Sorting in Java](https://www.baeldung.com/java-sorting)**  
  Tutorial Java yang membahas proses sorting menggunakan `Comparator`, `Comparable`, dan berbagai pendekatan sorting. Relevan untuk memahami bagaimana data dapat diurutkan dalam aplikasi Java.

- **[Oracle Java Documentation — Collections](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Collections.html)**  
  Dokumentasi resmi Java mengenai `Collections.sort()` dan berbagai operasi pada collection. Berguna sebagai referensi ketika membandingkan algoritma sorting yang dibuat sendiri dengan fitur sorting bawaan Java.

- **[Programiz — Java Programming](https://www.programiz.com/java-programming/arrays)**  
  Materi dasar Java mengenai array dan pengurutan data, cocok untuk memahami konsep sorting sebelum diterapkan pada data transaksi.

- **[VisuAlgo — Sorting](https://visualgo.net/en/sorting)**  
  Menyediakan animasi visual step-by-step untuk memahami proses **Bubble Sort, Selection Sort, Insertion Sort, Merge Sort, Quick Sort,** dan **Heap Sort**. Cocok sebagai pelengkap untuk melihat cara kerja algoritma secara visual.

### Big O Notation

- **[Big-O Cheat Sheet](https://www.bigocheatsheet.com/)**  
  Tabel referensi **Time Complexity** dan **Space Complexity** untuk berbagai algoritma, termasuk algoritma sorting. Menampilkan kompleksitas **Best Case, Average Case,** dan **Worst Case**.

- **[freeCodeCamp — Big O Notation Explained](https://www.freecodecamp.org/news/big-o-notation-why-it-matters-and-why-it-doesnt-1674cfa8a23c/)**  
  Penjelasan Big O secara sederhana dan cocok untuk pemula dalam memahami bagaimana kompleksitas algoritma dianalisis.

- **[Stanford — Algorithms Specialization](https://www.coursera.org/specializations/algorithms)**  
  Materi algoritma yang membahas **asymptotic notation** dan analisis kompleksitas algoritma secara lebih mendalam.

- **[MIT OpenCourseWare — Introduction to Algorithms](https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-spring-2020/)**  
  Materi pembelajaran algoritma dari MIT yang mencakup analisis algoritma, asymptotic notation, serta berbagai konsep dasar algoritma.

### Rekomendasi Penggunaan

| Topik | Sumber yang Direkomendasikan |
|---|---|
| Implementasi Sorting dengan Java | GeeksforGeeks |
| Sorting pada Java Collection | Baeldung & Oracle |
| Dasar Java untuk Sorting | Programiz |
| Visualisasi Sorting | VisuAlgo |
| Time & Space Complexity | Big-O Cheat Sheet |
| Pemahaman Big O untuk Pemula | freeCodeCamp |
| Analisis Algoritma Mendalam | Stanford & MIT |

> **Catatan:** Sumber-sumber di atas digunakan sebagai referensi pembelajaran untuk mendukung pemahaman konsep **Sorting Algorithms** dan **Big O Notation** yang diterapkan pada project **Java Spring Boot Sorting Algorithm & Benchmark**.