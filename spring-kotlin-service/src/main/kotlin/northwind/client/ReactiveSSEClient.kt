package northwind.client

import northwind.model.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux

import java.time.LocalTime

@Component
open class ReactiveSSEClient {

    public fun serverSentOrders(): Flux<ServerSentEvent<Order>> {
            val client = WebClient.create("http://localhost:8080/")
            val type = object : ParameterizedTypeReference<ServerSentEvent<Order>>() {

            }

            return client.get()
                .uri("/order-stream")
                .retrieve()
                .bodyToFlux<ServerSentEvent<Order>>(type)
        }
}
