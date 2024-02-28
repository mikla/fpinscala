package application.minio

import io.minio.{GetObjectArgs, ListObjectsArgs, MinioClient, UploadObjectArgs}

import scala.collection.mutable.ListBuffer

object MinioClientApp extends App {

  val minioClient =
    MinioClient.builder()
      .endpoint("http://localhost:9001")
      .credentials("local_minio", "local_minio123")
      .build()

//  minioClient.makeBucket(MakeBucketArgs.builder().bucket("exchange").build())

  minioClient.listBuckets().forEach(b => println(b.name()))

  (174 to 180).foreach { id =>
    minioClient.uploadObject(UploadObjectArgs.builder()
      .bucket("exchange")
      .`object`(s"ml/$id/banner/test.txt")
      .filename("/Users/user/Downloads/test.txt")
      .build())

//    minioClient.set(SetObjectAclArgs.builder()
//      .bucket("exchange")
//      .`object`("your/object/path")
//      .acl("public-read")
//      .build())
  }

  val f = minioClient.getObject(GetObjectArgs.builder()
    .bucket("exchange")
    .`object`("obd/test.txt")
    .build()).readAllBytes()

  val list = minioClient.listObjects(ListObjectsArgs
    .builder()
    .bucket("exchange")
    .prefix("obd/")
//    .recursive(true)
    .build())

  list.forEach(o => println(o.get().objectName().stripPrefix("obd/").init))

  println(f.length)

  val allObjects = ListBuffer[String]()

  val iterator = minioClient.listObjects(
    ListObjectsArgs.builder().bucket("exchange").prefix("obd/").build())

  iterator.forEach(o => allObjects += o.get().objectName())

  println(allObjects.toList)

}
