package northwind.util

import java.io.IOException
import java.util.stream.StreamSupport

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode

import northwind.model.Address
import northwind.model.Customer
import northwind.model.Product
import northwind.util.CustomerExtractor.extractCustomerAddress

/*
{
"CustomerID": "ALFKI",
"CompanyName": "Alfreds Futterkiste",
"ContactName": "Maria Anders",
"ContactTitle": "Sales Representative",
"Address": "Obere Str. 57",
"City": "Berlin",
"Region": null,
"PostalCode": "12209",
"Country": "Germany",
"Phone": "030-0074321",
"Fax": "030-0076545"
}
*/
object CustomerExtractor {

    fun extractCustomer(jsonString: String): Customer? {
        val parent: JsonNode
        try {
            parent = ObjectMapper().readTree(jsonString)
            val node = parent.get("value")
            if (node is ArrayNode) {
                val customers = node as ArrayNode
                return StreamSupport.stream(customers.spliterator(), false).map({ customerNode ->
                    val nameParts = customerNode.get("ContactName").asText().split(" ")
                    val c = Customer()
                    c.firstName = nameParts[0]
                    c.lastName = nameParts[nameParts.size - 1]
                    c.id = customerNode.get("CustomerID").asText()
                    c.address = extractCustomerAddress(customerNode)
                    c
                }).findFirst().orElseThrow({ IOException() })
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun extractCustomerAddress(addressNode: JsonNode): Address {
        val address = Address()
        address.address = addressNode.get("Address").asText()
        address.city = addressNode.get("City").asText()
        address.country = addressNode.get("Country").asText()
        address.phone = addressNode.get("Phone").asText()
        address.zip = addressNode.get("PostalCode").asText()
        return address
    }
}
