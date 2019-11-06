package northwind.service.impl

import java.util.HashMap

import northwind.util.NorthwindUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse

import northwind.client.ReactiveHttpClient
import northwind.exception.CoreException
import northwind.model.Product
import northwind.service.api.IProductService
import northwind.util.ProductExtractor
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

@Service
open class ProductService : IProductService {

    @Autowired
    internal var schedular: Scheduler? = null

    @Autowired
    internal var httpClient: ReactiveHttpClient? = null


    override public fun getProduct(productId: Int?): Mono<Product> {
        val headers = mutableMapOf<String,String>()
        val queryParams = mutableMapOf<String,String>()
        queryParams.put("\$filter", "ProductID eq $productId")
        return Mono.create({ emitter ->
            try {
                val response =
                    httpClient!!.request(NorthwindUtil.URL, "Products", HttpMethod.GET, headers, queryParams, null)
                response.subscribeOn(schedular)
                response.subscribe({ clientResponse: ClientResponse ->
                    clientResponse.bodyToMono(String::class.java).subscribe({ json ->
                        val product = ProductExtractor.extractProduct(json)
                        emitter.success(product)
                    })

                })
            } catch (e: CoreException) {
                e.printStackTrace()
                emitter.error(e)
            }
        })

    }

}
