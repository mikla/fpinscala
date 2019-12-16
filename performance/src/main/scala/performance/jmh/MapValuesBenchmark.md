```sbtshell
jmh:run -i 3 -wi 3 -f1 -t1 .*MapValuesBenchmark.*
```

```sbtshell
[info] Benchmark                               Mode  Cnt      Score       Error   Units
[info] MapValuesBenchmark.mapTest             thrpt    3      0.022 ±     0.005  ops/ms
[info] MapValuesBenchmark.mapValuesForceTest  thrpt    3      0.024 ±     0.005  ops/ms
[info] MapValuesBenchmark.mapValuesTest       thrpt    3  68759.359 ± 17174.075  ops/ms

[info] MapValuesBenchmark.mapTest              avgt    3     42.385 ±    18.362   ms/op
[info] MapValuesBenchmark.mapValuesForceTest   avgt    3     43.272 ±     8.572   ms/op
[info] MapValuesBenchmark.mapValuesTest        avgt    3     ≈ 10⁻⁵               ms/op
```

