package com.github.j5ik2o.akka.persistence.dynamodb
import java.time.{Duration => JavaDuration}

import com.github.j5ik2o.akka.persistence.dynamodb.config.{JournalPluginConfig, PluginConfig}
import software.amazon.awssdk.http.Protocol
import software.amazon.awssdk.http.nio.netty.{NettyNioAsyncHttpClient, SdkEventLoopGroup}

object HttpClientUtils {

  def asyncBuilder(pluginConfig: PluginConfig): NettyNioAsyncHttpClient.Builder = {
    val result       = NettyNioAsyncHttpClient.builder()
    val clientConfig = pluginConfig.clientConfig
    clientConfig.maxConcurrency.foreach(v => result.maxConcurrency(v))
    clientConfig.maxPendingConnectionAcquires.foreach(v => result.maxPendingConnectionAcquires(v))
    clientConfig.readTimeout.foreach(v => result.readTimeout(JavaDuration.ofMillis(v.toMillis)))
    clientConfig.writeTimeout.foreach(v => result.writeTimeout(JavaDuration.ofMillis(v.toMillis)))
    clientConfig.connectionTimeout.foreach(
      v => result.connectionTimeout(JavaDuration.ofMillis(v.toMillis))
    )
    clientConfig.connectionAcquisitionTimeout.foreach(
      v => result.connectionAcquisitionTimeout(JavaDuration.ofMillis(v.toMillis))
    )
    clientConfig.connectionTimeToLive.foreach(
      v => result.connectionTimeToLive(JavaDuration.ofMillis(v.toMillis))
    )
    clientConfig.maxIdleConnectionTimeout.foreach(
      v => result.connectionMaxIdleTime(JavaDuration.ofMillis(v.toMillis))
    )
    clientConfig.useConnectionReaper.foreach(v => result.useIdleConnectionReaper(v))
    clientConfig.userHttp2.foreach(
      v => if (v) result.protocol(Protocol.HTTP2) else result.protocol(Protocol.HTTP1_1)
    )
    clientConfig.maxHttp2Streams.foreach(v => result.maxHttp2Streams(v))
    clientConfig.threadsOfEventLoopGroup.foreach(v =>
      result.eventLoopGroup(SdkEventLoopGroup.builder().numberOfThreads(v).build())
    )
    result
  }

}
