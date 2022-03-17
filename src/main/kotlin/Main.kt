import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

fun main() {
    println("starting up")

    val manager = Manager()
    val server = Server(manager)
    val instrumentStream = InstrumentStream()
    val quoteStream = QuoteStream()

    instrumentStream.connect { event ->
        manager.instrumentService.processInstrumentEvent(event)
    }

    quoteStream.connect { event ->
        manager.quoteService.processQuoteEvent(event)
    }


    server.start()
}

val jackson: ObjectMapper =
    jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
