```sbtshell
jmh:run -i 3 -wi 3 -f1 -t1 .*CollectionSearch.*
```


```sbtshell
[info] CollectionSearch.customBinarySearchOnSortedArray        avgt    3     5.294 ±   0.073  ms/op
[info] CollectionSearch.findOnListWithoutCatsEq                avgt    3  1491.676 ± 232.964  ms/op
[info] CollectionSearch.findOnListWithoutWithCatsEq            avgt    3  2023.981 ±  83.849  ms/op
[info] CollectionSearch.findOnVector                           avgt    3   584.618 ±  72.193  ms/op
[info] CollectionSearch.scalaBuiltInBinarySearchOnSortedArray  avgt    3     7.437 ±   1.125  ms/op
[info] CollectionSearch.useMap                                 avgt    3    10.035 ±   0.416  ms/op
```