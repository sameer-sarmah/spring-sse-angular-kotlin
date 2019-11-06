package northwind.util

import java.io.IOException
import java.util.stream.Collectors
import java.util.stream.StreamSupport

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

import northwind.model.OrderItems
import northwind.model.Product

/*
{
"OrderID": 10248,
"ProductID": 11,
"UnitPrice": "14.0000",
"Quantity": 12,
"Discount": 0
}
*/
object OrderItemExtractor {
    fun extractOrderItems(jsonString: String): List<OrderItems>? {
        val parent: JsonNode
        try {
            parent = ObjectMapper().readTree(jsonString)
            val arrayNode = parent.get("value")
            if (arrayNode is ArrayNode) {
                val orderItemsNode = arrayNode as ArrayNode
                return StreamSupport.stream(orderItemsNode.spliterator(), false).map({ node ->
                    val oi = OrderItems()
                    oi.orderId = node.get("OrderID").asInt();
                    val p = Product()
                    p.id =node.get("ProductID").asInt()
                    oi.product = p
                    oi.price = node.get("UnitPrice").asDouble()
                    oi.quantity = node.get("Quantity").asDouble()
                    oi.discount = node.get("Discount").asDouble()
                    oi
                }).collect(Collectors.toList())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}
