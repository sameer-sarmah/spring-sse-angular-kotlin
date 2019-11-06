package northwind.util

import org.springframework.stereotype.Component

@Component
open class OrderIdsCache {
    var orderIds: MutableList<Int>? = null
}
