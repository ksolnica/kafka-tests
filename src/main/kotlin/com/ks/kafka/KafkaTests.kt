package com.ks.kafka

import org.apache.kafka.streams.KafkaStreams
import java.util.concurrent.CountDownLatch

class ShutdownHook(
    private val streamer: KafkaStreams,
    private val cdl: CountDownLatch
) : Thread("shutdown-thread") {
    override fun run() {
        streamer.close()
        cdl.countDown()
    }
}

object KafkaTests {
    @JvmStatic
    fun main(args: Array<String>) {
        val streamer = Streamer.stream("input", "output")
        val cdl = CountDownLatch(1)

        Runtime.getRuntime().addShutdownHook(ShutdownHook(streamer, cdl))

        streamer.start()
        cdl.await()
    }
}