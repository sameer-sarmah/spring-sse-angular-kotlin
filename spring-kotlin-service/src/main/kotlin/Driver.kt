import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Arrays
import java.util.stream.Collectors

import northwind.client.ReactiveSSEClient
import northwind.model.Order
import northwind.service.api.IOrderService
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

import northwind.config.AppConfig
import northwind.config.CorsConfig
import northwind.model.Customer
import northwind.model.OrderItems
import northwind.model.Product
import northwind.service.api.ICustomerService
import northwind.service.api.IOrderItemService
import northwind.service.api.IProductService
import org.springframework.http.codec.ServerSentEvent
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2




    @Throws(InterruptedException::class)
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(AppConfig::class.java, CorsConfig::class.java)

        val dateStr = "1996-07-04T00:00:00"
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val zonedDateTime = LocalDateTime.parse(dateStr, formatter)
        System.out.println(zonedDateTime)

        val productService = ctx.getBean(IProductService::class.java)
        val productMono = productService.getProduct(1)

        val customerService = ctx.getBean(ICustomerService::class.java)
        val customerMono = customerService.getCustomer("ALFKI")

        val orderItemsSvc = ctx.getBean(IOrderItemService::class.java)
        val oiMono = orderItemsSvc.getOrderItems(10248)

        val tuple = Mono.zip(productMono, customerMono)
        tuple.subscribe({ t ->
            val product = t.getT1()
            val customer = t.getT2()
            System.out.println("Customer first name " + customer.firstName)
            System.out.println("Product name " + product.name)
        })

        oiMono.subscribe({ orderItems ->
            for (item in orderItems) {
                System.out.println(item.product?.name)
            }
        })

        val orderService = ctx.getBean(IOrderService::class.java)
        val orderMono = orderService.getOrder(10248)
        orderMono.subscribe({ order ->
            val orderItems = order.orderItems
            orderItems?.forEach({
                System.out.println(it.product?.name)
            })
        })
        val orderIdsMono = orderService.orderIds
        orderIdsMono.subscribe({ orderIds ->
            Flux.fromIterable(orderIds)
                .take(5)
                .delayElements(Duration.ofSeconds(5))
                .flatMap({ orderId -> orderService.getOrder(orderId) })
                .subscribe({ order ->
                    System.out.println(order)

                })
        })

        val sseClient = ctx.getBean(ReactiveSSEClient::class.java)
        val orderStream = sseClient.serverSentOrders()
        orderStream.subscribe({ sse ->
            System.out.println("order id: " + sse.id())
            System.out.println(sse.data())
        })

        Thread.sleep(100000)
    }


