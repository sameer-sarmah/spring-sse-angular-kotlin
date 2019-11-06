package northwind.service.api

import northwind.model.OrderItems
import reactor.core.publisher.Mono

interface IOrderItemService {
    fun getOrderItems(orderId: Int): Mono<MutableList<OrderItems>>
}
