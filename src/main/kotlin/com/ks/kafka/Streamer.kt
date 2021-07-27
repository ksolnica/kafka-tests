package com.ks.kafka

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import java.util.*

object Streamer {

    fun stream(from: String, to: String): KafkaStreams {
        return KafkaStreams(forwardTopology(from, to), properties())
    }

    fun forwardTopology(from: String, to: String): Topology {
        val builder = StreamsBuilder()
        builder.stream<String, String>(from).to(to)
        return builder.build()
    }

    fun properties(): Properties {
        val props = Properties()
        props[StreamsConfig.APPLICATION_ID_CONFIG] = "kafka-tests"
        props[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG] = 0
        props[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
        props[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String().javaClass
        return props
    }
}