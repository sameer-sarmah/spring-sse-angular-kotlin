package northwind.controller

import northwind.model.Customer
import northwind.service.api.ICustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
public open class CustomerController {

    @Autowired
    internal var customerService: ICustomerService? = null

    @RequestMapping(value = arrayOf("/customer/{customerId}"), method = arrayOf(RequestMethod.GET))
    public fun getCustomer(@PathVariable("customerId") customerId: String): Mono<Customer> {
        return customerService!!.getCustomer(customerId)
    }
}
