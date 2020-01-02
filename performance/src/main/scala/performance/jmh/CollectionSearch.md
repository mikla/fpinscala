```sbtshell
jmh:run -i 3 -wi 3 -f1 -t1 .*CollectionSearch.*
```


```sbtshell
[info] CollectionSearch.customBinarySearchOnSortedArray        avgt    3     5.544 ±    0.643  ms/op
[info] CollectionSearch.findOnListWithoutCatsEq                avgt    3  1408.519 ± 1061.995  ms/op
[info] CollectionSearch.findOnListWithoutWithCatsEq            avgt    3  2091.854 ±   66.168  ms/op
[info] CollectionSearch.findOnVector                           avgt    3   563.677 ±  128.248  ms/op
[info] CollectionSearch.scalaBuiltInBinarySearchOnSortedArray  avgt    3     8.260 ±    0.905  ms/op
```