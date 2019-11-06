package northwind.service.impl

import northwind.client.ReactiveHttpClient
import northwind.exception.CoreException
import northwind.model.Customer
import northwind.service.api.ICustomerService
import northwind.util.CustomerExtractor
import northwind.util.NorthwindUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import reactor.core.publisher.Mono
import reactor.core.scheduler.Scheduler

@Service
open class CustomerService : ICustomerService {

    @Autowired
    internal var schedular: Scheduler? = null

    @Autowired
    internal var httpClient: ReactiveHttpClient? = null

    public override fun getCustomer(customerId: String?): Mono<Customer> {
        val headers = mutableMapOf<String,String>()
        val queryParams = mutableMapOf<String,String>()
        queryParams.put("\$filter", "CustomerID eq '$customerId'")
        return Mono.create({ emitter ->
            try {
                val response =
                    httpClient!!.request(NorthwindUtil.URL, "Customers", HttpMethod.GET, headers, queryParams, null)
                System.out.println("Non blocking")
                response.subscribeOn(schedular)
                response.subscribe({ clientResponse: ClientResponse ->
                    clientResponse.bodyToMono(String::class.java).subscribe({ json ->
                        val customer = CustomerExtractor.extractCustomer(json)
                        emitter.success(customer)
                    })

                })
            } catch (e: CoreException) {
                e.printStackTrace()
                emitter.error(e)
            }
        })

    }
}
