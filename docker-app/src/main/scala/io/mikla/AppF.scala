package io.mikla

import java.lang.management.ManagementFactory

import ai.catboost.CatBoostModel

import scala.jdk.CollectionConverters.CollectionHasAsScala

object AppF extends App {

  val runtimeMxBean = ManagementFactory.getRuntimeMXBean
  val arguments = runtimeMxBean.getInputArguments.asScala

  arguments.foreach(println)

  // print commited heap size
  println("Committed heap size: " + Runtime.getRuntime.totalMemory() / 1024 / 1024 + " MB")

  while (true) {

    println("App started. Waiting 20 seconds before loading the model.")
    Thread.sleep(20000)

    val inputStream = getClass.getResourceAsStream("/latest-1.cbm")
    val modelx = CatBoostModel.loadModel(inputStream)
    println(modelx.getMetadata.size())
    inputStream.close()

    println("Waiting before model becomes available for GC")
    Thread.sleep(10000)

    println(modelx.getMetadata.size())

    System.gc()
  }

}
