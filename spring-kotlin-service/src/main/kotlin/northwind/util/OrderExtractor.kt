package northwind.util

import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.stream.Collector
import java.util.stream.Collectors
import java.util.stream.StreamSupport

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

import northwind.model.Address
import northwind.model.Order
import northwind.util.OrderExtractor.extractShipperAddress
import northwind.util.OrderExtractor.parse

/*
{
"OrderID": 10248,
"CustomerID": "VINET",
"EmployeeID": 5,
"OrderDate": "1996-07-04T00:00:00",
"RequiredDate": "1996-08-01T00:00:00",
"ShippedDate": "1996-07-16T00:00:00",
"ShipVia": 3,
"Freight": "32.3800",
"ShipName": "Vins et alcools Chevalier",
"ShipAddress": "59 rue de l'Abbaye",
"ShipCity": "Reims",
"ShipRegion": null,
"ShipPostalCode": "51100",
"ShipCountry": "France"
}
*/
object OrderExtractor {

    fun extractOrder(jsonString: String): Order? {
        val parent: JsonNode
        try {
            parent = ObjectMapper().readTree(jsonString)
            val node = parent.get("value")
            if (node is ArrayNode) {
                val orders = node as ArrayNode
                return StreamSupport.stream(orders.spliterator(), false).map({ orderNode ->
                    val o = Order()
                    o.requiredDate = orderNode.get("RequiredDate").asText()
                    o.shippedDate = orderNode.get("ShippedDate").asText()
                    o.orderDate = orderNode.get("OrderDate").asText()
                    o.id = orderNode.get("OrderID").asInt()
                    o.address = extractShipperAddress(orderNode)
                    o
                }).findFirst().orElseThrow({ IOException() })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun extractShipperAddress(addressNode: JsonNode): Address {
        val address = Address()
        address.address = addressNode.get("ShipAddress").asText()
        address.city = addressNode.get("ShipCity").asText()
        address.country = addressNode.get("ShipCountry").asText()
        address.state = addressNode.get("ShipRegion").asText()
        address.zip = addressNode.get("ShipPostalCode").asText()
        return address
    }

    fun parse(dateStr: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        return LocalDateTime.parse(dateStr, formatter)
    }

    fun extractOrderIds(jsonString: String): MutableList<Int>? {
        val parent: JsonNode
        try {
            parent = ObjectMapper().readTree(jsonString)
            val node = parent.get("value")
            if (node is ArrayNode) {
                val orderIds = mutableListOf<Int>()
                val orders = node as ArrayNode
                orders.spliterator().forEachRemaining({
                    orderIds.add(it.get("OrderID").asInt())
                })
                return orderIds
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}
