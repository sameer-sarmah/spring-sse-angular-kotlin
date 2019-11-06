package northwind.service.api

import northwind.model.Product
import reactor.core.publisher.Mono

interface IProductService {
    fun getProduct(productId: Int?): Mono<Product>
}
