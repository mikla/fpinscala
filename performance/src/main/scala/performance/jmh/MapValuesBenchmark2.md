```sbtshell
jmh:run -i 3 -wi 3 -f1 -t1 .*MapValuesBenchmark2.*
```

```sbtshell

[info] Benchmark                                                            Mode     Cnt      Score       Error   Units
[info] MapValuesBenchmark2.mapTest                                         thrpt       3      0.052 ±     0.039  ops/ms
[info] MapValuesBenchmark2.mapValuesForceTest                              thrpt       3      0.050 ±     0.033  ops/ms
[info] MapValuesBenchmark2.mapValuesTest                                   thrpt       3  64774.830 ± 53714.467  ops/ms
[info] MapValuesBenchmark2.mapTest                                          avgt       3     19.586 ±     8.138   ms/op
[info] MapValuesBenchmark2.mapValuesForceTest                               avgt       3     18.901 ±     0.400   ms/op
[info] MapValuesBenchmark2.mapValuesTest                                    avgt       3     ≈ 10⁻⁵               ms/op
[info] MapValuesBenchmark2.mapTest                                        sample    1627     18.448 ±     0.109   ms/op
[info] MapValuesBenchmark2.mapTest:mapTest·p0.00                          sample             17.302               ms/op
[info] MapValuesBenchmark2.mapTest:mapTest·p0.50                          sample             17.957               ms/op
[info] MapValuesBenchmark2.mapTest:mapTest·p0.90                          sample             19.726               ms/op
[info] MapValuesBenchmark2.mapTest:mapTest·p0.95                          sample             21.168               ms/op
[info] MapValuesBenchmark2.mapTest:mapTest·p0.99                          sample             23.932               ms/op
[info] MapValuesBenchmark2.mapTest:mapTest·p0.999                         sample             30.643               ms/op
[info] MapValuesBenchmark2.mapTest:mapTest·p0.9999                        sample             31.261               ms/op
[info] MapValuesBenchmark2.mapTest:mapTest·p1.00                          sample             31.261               ms/op
[info] MapValuesBenchmark2.mapValuesForceTest                             sample    1551     19.357 ±     0.179   ms/op
[info] MapValuesBenchmark2.mapValuesForceTest:mapValuesForceTest·p0.00    sample             17.727               ms/op
[info] MapValuesBenchmark2.mapValuesForceTest:mapValuesForceTest·p0.50    sample             18.317               ms/op
[info] MapValuesBenchmark2.mapValuesForceTest:mapValuesForceTest·p0.90    sample             21.882               ms/op
[info] MapValuesBenchmark2.mapValuesForceTest:mapValuesForceTest·p0.95    sample             23.560               ms/op
[info] MapValuesBenchmark2.mapValuesForceTest:mapValuesForceTest·p0.99    sample             28.849               ms/op
[info] MapValuesBenchmark2.mapValuesForceTest:mapValuesForceTest·p0.999   sample             34.702               ms/op
[info] MapValuesBenchmark2.mapValuesForceTest:mapValuesForceTest·p0.9999  sample             35.389               ms/op
[info] MapValuesBenchmark2.mapValuesForceTest:mapValuesForceTest·p1.00    sample             35.389               ms/op
[info] MapValuesBenchmark2.mapValuesTest                                  sample  791304     ≈ 10⁻⁴               ms/op
[info] MapValuesBenchmark2.mapValuesTest:mapValuesTest·p0.00              sample             ≈ 10⁻⁵               ms/op
[info] MapValuesBenchmark2.mapValuesTest:mapValuesTest·p0.50              sample             ≈ 10⁻⁴               ms/op
[info] MapValuesBenchmark2.mapValuesTest:mapValuesTest·p0.90              sample             ≈ 10⁻⁴               ms/op
[info] MapValuesBenchmark2.mapValuesTest:mapValuesTest·p0.95              sample             ≈ 10⁻⁴               ms/op
[info] MapValuesBenchmark2.mapValuesTest:mapValuesTest·p0.99              sample             ≈ 10⁻⁴               ms/op
[info] MapValuesBenchmark2.mapValuesTest:mapValuesTest·p0.999             sample             ≈ 10⁻⁴               ms/op
[info] MapValuesBenchmark2.mapValuesTest:mapValuesTest·p0.9999            sample              0.008               ms/op

```