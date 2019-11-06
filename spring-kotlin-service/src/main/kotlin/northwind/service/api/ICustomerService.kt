package northwind.service.api

import northwind.model.Customer
import reactor.core.publisher.Mono

interface ICustomerService {
    fun getCustomer(customerId: String?): Mono<Customer>
}
