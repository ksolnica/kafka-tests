package com.ks.kafka

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.TopologyTestDriver
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StreamerTest {

    @Test
    fun shouldStreamFromInputToOutput() {
        val test = TopologyTestDriver(Streamer.forwardTopology(INPUT, OUTPUT), Streamer.properties())
        val input = test.createInputTopic(INPUT, Serdes.String().serializer(), Serdes.String().serializer())
        val output = test.createOutputTopic(OUTPUT, Serdes.String().deserializer(), Serdes.String().deserializer())
        input.pipeInput("key", "value")
        assertThat(output.readKeyValue()).isEqualTo(KeyValue("key", "value"))
        assertThat(output.isEmpty).isTrue
        test.close()
    }

    @Test
    fun shouldStreamAggregateFromInputToOutput() {
        val test = TopologyTestDriver(Streamer.aggregateTopology(INPUT, OUTPUT), Streamer.properties())
        val input = test.createInputTopic(INPUT, Serdes.String().serializer(), Serdes.String().serializer())
        val output = test.createOutputTopic(OUTPUT, Serdes.String().deserializer(), Serdes.Long().deserializer())
        input.pipeInput("key", "value1")
        input.pipeInput("key", "value2")
        input.pipeInput("key", "value1")
        assertThat(output.readKeyValue()).isEqualTo(KeyValue("value1", 1L))
        assertThat(output.readKeyValue()).isEqualTo(KeyValue("value2", 1L))
        assertThat(output.readKeyValue()).isEqualTo(KeyValue("value1", 2L))
        assertThat(output.isEmpty).isTrue
        test.close()
    }

    companion object {
        const val INPUT = "input"
        const val OUTPUT = "output"
    }
}