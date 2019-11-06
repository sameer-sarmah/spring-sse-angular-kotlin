package northwind.util

import java.io.IOException
import java.util.stream.StreamSupport

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

import northwind.model.Product

/*
{
	"value": [
	{
	"ProductID": 1,
	"ProductName": "Chai",
	"SupplierID": 1,
	"CategoryID": 1,
	"QuantityPerUnit": "10 boxes x 20 bags",
	"UnitPrice": "18.0000",
	"UnitsInStock": 39,
	"UnitsOnOrder": 0,
	"ReorderLevel": 10,
	"Discontinued": false
	}
	]
}
*/
object ProductExtractor {

    fun extractProduct(jsonString: String): Product? {
        val parent: JsonNode
        try {
            parent = ObjectMapper().readTree(jsonString)
            val productsNode = parent.get("value")
            if (productsNode is ArrayNode) {
                val products = productsNode as ArrayNode
                return StreamSupport.stream(products.spliterator(), false).map({ node ->
                    val p = Product()
                    p.id = node.get("ProductID").asInt()
                    p.name = node.get("ProductName").asText()
                    p.isDiscontinued =node.get("Discontinued").asBoolean()
                    p.quantityPerUnit = node.get("QuantityPerUnit").asText()
                    p.unitPrice =node.get("UnitPrice").asText().toDouble()
                    p
                }).findFirst().orElseThrow({ IOException() })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }
}
