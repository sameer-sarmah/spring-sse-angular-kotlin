package northwind.controller

import northwind.model.Product
import northwind.service.api.IProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
public open class ProductController {

    @Autowired
    internal var productService: IProductService? = null

    @RequestMapping(path = arrayOf("/product/{productId}"), method = arrayOf(RequestMethod.GET))
    public fun getProduct(@PathVariable("productId") productId: Int): Mono<Product> {
        return productService!!.getProduct(productId)
    }
}
