package northwind.model


import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import java.time.LocalDateTime


class Order {

    var id: Int = 0

    var customer: Customer? = null
    var address: Address? = null

    var orderDate: String? = null

    var shippedDate: String? = null

    var requiredDate: String? = null

    var shippingFee: Double = 0.toDouble()
    var taxes: Double = 0.toDouble()
    var paymentType: String? = null
    var orderItems: List<OrderItems>? = null

}
