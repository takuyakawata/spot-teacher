package com.spotteacher.admin.config.auth

// @Component
// class SecurityFilterWithCognito : WebFilter {
// //    @Value("\${api.secret-key}")
//    private lateinit var secretKey: String
//
//    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
//        val requestPath = exchange.request.uri.path
//        if (requestPath != "/graphql" && requestPath != "/subscriptions") {
//            return chain.filter(exchange)
//        }
//
//        return DataBufferUtils.join(exchange.request.body)
//            .flatMap { dataBuffer ->
//                val bodyBytes = ByteArray(dataBuffer.readableByteCount())
//                dataBuffer.read(bodyBytes)
//                DataBufferUtils.release(dataBuffer)
//
//                val bodyString = String(bodyBytes)
//                val query = extractQuery(bodyString)
//                logInfo { "GraphQL Query: $query" }
//
//                // HMACの検証ロジックだが、サンプルなので実装していない
//                // 　一旦不要なため、コメントアウト
// //                val clientHmac = exchange.request.headers.getFirst("X-HMAC-Signature") ?: ""
// //                if (clientHmac != generateHmac(query, secretKey)) {
// //                    logWarn { "HMAC verification failed. Query: $query" }
// //                    exchange.response.statusCode = HttpStatus.UNAUTHORIZED
// //                    val buffer = exchange.response.bufferFactory().wrap("Invalid HMAC signature".toByteArray())
// //                    return@flatMap exchange.response.writeWith(Mono.just(buffer))
// //                }
//
//                val cachedRequest = CachedBodyServerHttpRequest(exchange.request, bodyBytes, exchange)
//                val mutatedExchange = exchange.mutate().request(cachedRequest).build()
//                chain.filter(mutatedExchange)
//            }
//    }
//
//    private fun extractQuery(requestBody: String): String {
//        val regex = """"query"\s*:\s*"(.*?)"""".toRegex()
//        val match = regex.find(requestBody)
//        return match?.groupValues?.get(1)
//            ?.replace("\\n", " ")
//            ?.lines()
//            ?.joinToString(" ") { it.trim() }
//            ?.replace(Regex("\\s{2,}"), " ")
//            ?.trim()
//            ?: ""
//    }
//
// //    private fun generateHmac(query: String, secretKey: String): String {
// //        val hmacSha256 = "HmacSHA256"
// //        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), hmacSha256)
// //        val mac = Mac.getInstance(hmacSha256)
// //        mac.init(secretKeySpec)
// //        val hmacBytes = mac.doFinal(query.toByteArray())
// //        return Base64.getEncoder().encodeToString(hmacBytes)
// //    }
// }
//
// class CachedBodyServerHttpRequest(
//    delegate: ServerHttpRequest,
//    private val bodyBytes: ByteArray,
//    private val exchange: ServerWebExchange
// ) : ServerHttpRequestDecorator(delegate) {
//
//    override fun getBody(): Flux<DataBuffer> {
//        val bufferFactory = exchange.response.bufferFactory()
//        return Flux.defer {
//            val buffer = bufferFactory.wrap(bodyBytes)
//            Flux.just(buffer)
//        }
//    }
// }
