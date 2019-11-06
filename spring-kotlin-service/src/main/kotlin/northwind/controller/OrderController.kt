package northwind.controller

import northwind.model.Order
import northwind.service.api.IOrderService
import northwind.util.OrderIdsCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@RestController
public open class OrderController {

    @Autowired
    private val orderIdsCache: OrderIdsCache? = null

    @Autowired
    private val orderService: IOrderService? = null


    @GetMapping(path = arrayOf("/order-stream"), produces = arrayOf(MediaType.TEXT_EVENT_STREAM_VALUE))
    public fun getOrderStream(): Flux<ServerSentEvent<Order>> {
        return Flux.fromIterable(orderIdsCache!!.orderIds)
            .delayElements(Duration.ofSeconds(5))
            .flatMap({ orderId -> orderService!!.getOrder(orderId) })
            .map({ order ->
                val sseOrder : ServerSentEvent<Order> =  ServerSentEvent.builder<Order>()
                    .id(order.id.toString())
                    .event("order-stream-event")
                    .data(order)
                    .build()
                sseOrder

            })
    }


    @RequestMapping(path = arrayOf("/order/{orderId}"), method = arrayOf(RequestMethod.GET))
    public fun getOrder(@PathVariable("orderId") orderId: Int): Mono<Order> {
        return orderService!!.getOrder(orderId)
    }

}
