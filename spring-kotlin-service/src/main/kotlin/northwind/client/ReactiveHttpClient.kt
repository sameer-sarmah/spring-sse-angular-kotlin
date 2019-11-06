package northwind.client

import java.net.URI
import java.util.function.Function

import org.apache.log4j.Logger
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec
import org.springframework.web.util.UriBuilder

import northwind.exception.CoreException
import reactor.core.publisher.Mono

@Component
open class ReactiveHttpClient {


    @Throws(CoreException::class)
    public fun request(
        url: String, entity: String, method: HttpMethod, headers: Map<String, String>,
        queryParams: Map<String, String>, jsonString: String?
    ): Mono<ClientResponse> {

        val client = WebClient.builder()
            .baseUrl(url)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build()
        client.method(HttpMethod.GET)
        val request = client.get()

        headers.forEach { (key, value) -> request.header(key, value) }
        val func = { uriBuilder: UriBuilder ->
            uriBuilder
                .path(entity)
                .queryParam("\$format", "json")
            queryParams.forEach { (key, value) ->
                uriBuilder.queryParam(key, value)

            }

            val uri = uriBuilder.build()
            println(uri)
            uri
        }
        return request.uri(func).exchange()

    }

    companion object {
        internal val logger = Logger.getLogger(ReactiveHttpClient::class.java!!)
    }

}
