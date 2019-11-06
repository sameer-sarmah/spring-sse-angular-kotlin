package northwind.model



class Product {
    var id: Int = 0
    var name: String? = null
    var description: String? = null
    var unitPrice: Double = 0.toDouble()
    var quantityPerUnit: String? = null
    var isDiscontinued: Boolean = false
}
