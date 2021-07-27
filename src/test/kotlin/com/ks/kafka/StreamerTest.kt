package com.ks.kafka

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.jupiter.api.Test

class StreamerTest {

    @Test
    fun shouldStreamFromInputToOutput() {
        val test = TopologyTestDriver(Streamer.forwardTopology(INPUT, OUTPUT), Streamer.properties())
        val input = test.createInputTopic(INPUT, Serdes.String().serializer(), Serdes.String().serializer())
        val output = test.createOutputTopic(OUTPUT, Serdes.String().deserializer(), Serdes.String().deserializer())
        input.pipeInput("key", "value")
        assert(output.readKeyValue() == KeyValue("key", "value"))
        assert(output.isEmpty)
        test.close()
    }

    companion object {
        const val INPUT = "input"
        const val OUTPUT = "output"
    }
}