package northwind.service.impl

import java.io.IOException
import java.util.*
import java.util.stream.Collectors
import java.util.stream.StreamSupport

import northwind.util.NorthwindUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

import northwind.client.ReactiveHttpClient
import northwind.exception.CoreException
import northwind.model.Customer
import northwind.model.Order
import northwind.model.OrderItems
import northwind.model.Product
import northwind.service.api.ICustomerService
import northwind.service.api.IOrderItemService
import northwind.service.api.IOrderService
import northwind.service.api.IProductService
import northwind.util.OrderExtractor
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler
import reactor.util.function.Tuple2

@Service
open class OrderService : IOrderService {

    @Autowired
    internal var schedular: Scheduler? = null

    @Autowired
    internal var httpClient: ReactiveHttpClient? = null

    @Autowired
    internal var productService: IProductService? = null

    @Autowired
    internal var customerService: ICustomerService? = null

    @Autowired
    internal var orderItemsSvc: IOrderItemService? = null

    override  public val orderIds: Mono<MutableList<Int>>
        @Override
        get() {
            val headers = mutableMapOf<String,String>()
            val queryParams = mutableMapOf<String,String>()
            queryParams.put("\$select", "OrderID")
            return Mono.create({ emitter ->
                try {
                    val response =
                        httpClient!!.request(NorthwindUtil.URL, "Orders", HttpMethod.GET, headers, queryParams, null)
                    response.subscribeOn(schedular)
                    response.subscribe({ clientResponse: ClientResponse ->
                        clientResponse.bodyToMono(String::class.java).subscribe({ json ->
                            val orderIds = OrderExtractor.extractOrderIds(json)
                            emitter.success(orderIds)
                        })

                    })
                } catch (e: CoreException) {
                    e.printStackTrace()
                    emitter.error(e)
                }
            })
        }


    override public fun getOrder(orderID: Int): Mono<Order> {

        val headers = mutableMapOf<String,String>()
        val queryParams = mutableMapOf<String,String>()
        queryParams.put("\$filter", "OrderID eq $orderID")

        return Mono.create({ emitter ->
            try {
                val oiMono = orderItemsSvc!!.getOrderItems(orderID)
                val response =
                    httpClient!!.request(NorthwindUtil.URL, "Orders", HttpMethod.GET, headers, queryParams, null)
                System.out.println("Non blocking")
                response.subscribeOn(schedular)
                response.subscribe({ clientResponse: ClientResponse ->
                    clientResponse.bodyToMono(String::class.java).subscribe({ json ->
                        val customerMono = customerService!!.getCustomer(extractCustomerID(json))

                        val order = OrderExtractor.extractOrder(json)
                        val tuple = Mono.zip(customerMono, oiMono)
                        tuple.subscribe({ t ->
                            val customer = t.getT1()
                            val orderItems = t.getT2()
                            order?.customer = customer
                            order?.orderItems = orderItems
                            emitter.success(order)
                        })

                    })

                })
            } catch (e: CoreException) {
                e.printStackTrace()
                emitter.error(e)
            }
        })



    }




    private fun extractCustomerID(jsonString: String): String? {
        val parent: JsonNode
        try {
            parent = ObjectMapper().readTree(jsonString)
            val node = parent.get("value")
            if (node is ArrayNode) {
                val orders = node as ArrayNode
                return StreamSupport.stream(orders.spliterator(), false)
                    .map({ orderNode -> orderNode.get("CustomerID").asText() }).findFirst()
                    .orElseThrow({ IOException() })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }


}
