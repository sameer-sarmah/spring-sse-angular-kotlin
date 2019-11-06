package northwind.service.api

import northwind.model.Order
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IOrderService {

    val orderIds: Mono<MutableList<Int>>
    fun getOrder(orderID: Int): Mono<Order>
}
