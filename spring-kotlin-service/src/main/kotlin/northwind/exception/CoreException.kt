package northwind.exception

class CoreException(
    override val message: String, val statusCode: Int
) : Exception(message)
