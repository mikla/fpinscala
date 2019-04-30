package application

import com.datastax.driver.core.policies.ExponentialReconnectionPolicy
import com.datastax.driver.core.{ConsistencyLevel, HostDistance, PoolingOptions, SocketOptions}
import com.outworkers.phantom.builder.query.QueryOptions
import com.outworkers.phantom.connectors.ContactPoints
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import scala.concurrent.duration._

object CassandraConnectionApp extends App {

  private[this] val poolingOptions = new PoolingOptions()
    .setMaxRequestsPerConnection(HostDistance.REMOTE, 8192)
    .setMaxQueueSize(8192)

  private[this] val queryOptions = new QueryOptions(Some(ConsistencyLevel.LOCAL_QUORUM), None)

  private[this] val reconnectionPolicy = new ExponentialReconnectionPolicy(2000, 20000)

  private[this] val socketOptions = new SocketOptions().setConnectTimeoutMillis(1.minute.toMillis.toInt)

  def connect() = {

    val connectionAttempt = Task.delay(
      ContactPoints(Seq("127.0.0.1"))
        .withClusterBuilder(
          _.withCredentials("cassandra", "cassandra")
            .withPoolingOptions(poolingOptions)
            .withQueryOptions(queryOptions.options)
            .withSocketOptions(socketOptions)
            .withReconnectionPolicy(reconnectionPolicy)
        ).keySpace("nevos_sharing", autoinit = false)
    )

    connectionAttempt.runSyncUnsafe()

  }

  print(connect().session)

}
