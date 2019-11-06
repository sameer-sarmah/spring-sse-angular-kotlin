package northwind.listener

import northwind.service.api.IOrderService
import northwind.util.OrderIdsCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
open class NorthwindApplicationListener : ApplicationListener<ApplicationEvent> {

    @Autowired
    private val orderService: IOrderService? = null

    @Autowired
    private val orderIdsCache: OrderIdsCache? = null


    override fun onApplicationEvent(event: ApplicationEvent) {
        if (event is ApplicationStartedEvent) {
            val orderIdsMono = orderService?.orderIds
            orderIdsMono?.subscribe({ orderIds -> orderIdsCache?.orderIds = orderIds })
        }
    }
}
