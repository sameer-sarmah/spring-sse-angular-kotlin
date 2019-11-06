package northwind.service.impl

import java.util.*
import java.util.stream.Collectors

import northwind.model.Product
import northwind.service.api.IProductService
import northwind.util.NorthwindUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse

import northwind.client.ReactiveHttpClient
import northwind.exception.CoreException
import northwind.model.OrderItems
import northwind.service.api.IOrderItemService
import northwind.util.OrderItemExtractor
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler


@Service
open class OrderItemService : IOrderItemService {

    @Autowired
    internal var schedular: Scheduler? = null

    @Autowired
    internal var httpClient: ReactiveHttpClient? = null

    @Autowired
    internal var productService: IProductService? = null


    override public fun getOrderItems(orderId: Int): Mono<MutableList<OrderItems>> {
        val headers = mutableMapOf<String,String>()
        val queryParams = mutableMapOf<String,String>()
        queryParams.put("\$filter", "OrderID eq $orderId")
        return Mono.create({ emitter ->
            try {
                val response =
                    httpClient!!.request(NorthwindUtil.URL, "Order_Details", HttpMethod.GET, headers, queryParams, null)
                response.subscribeOn(schedular)
                val productsMono = mutableListOf<Mono<Product>>()
                response.subscribe({ clientResponse: ClientResponse ->
                    clientResponse.bodyToMono(String::class.java).subscribe({ json ->
                        val orderItems = OrderItemExtractor.extractOrderItems(json)
                        orderItems?.forEach {
                            productsMono.add(productService!!.getProduct(it.product?.id))
                        }
                        Mono.zip(productsMono, { productsArray -> productsArray }).subscribe({ productsArray ->
                            val products = Arrays.stream(productsArray)
                                .filter({ product -> product is Product })
                                .map({ product -> product as Product })
                                .collect(Collectors.toList())
                            orderItems?.forEach {
                                val correspondingProduct = products.stream()
                                    .filter({ product -> product?.id === it.product?.id }).findFirst()
                                    .get()
                                it.product = correspondingProduct
                            }
                            emitter.success(orderItems?.toMutableList())
                        })

                    })

                })
            } catch (e: CoreException) {
                e.printStackTrace()
                emitter.error(e)
            }
        })
    }

}
