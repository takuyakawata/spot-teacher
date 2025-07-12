package com.spotteacher.backend.graphql


///**
// * GraphQLクエリのHMAC署名を生成する
// *
// * @param query 署名対象のクエリ文字列
// * @param secretKey 署名用の秘密鍵
// * @return 生成されたHMAC署名
// */
//private fun generateHmac(query: String, secretKey: String): String {
//    val hmacSha256 = "HmacSHA256"
//    val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), hmacSha256)
//    val mac = Mac.getInstance(hmacSha256)
//    mac.init(secretKeySpec)
//    val hmacBytes = mac.doFinal(query.toByteArray())
//    return Base64.getEncoder().encodeToString(hmacBytes)
//}
//
///**
// * GraphQLRequestをHTTPリクエストに変換して送信する
// *
// * @param graphQLRequest 送信するGraphQLRequest
// * @param secretKey HMAC署名用の秘密鍵
// * @param cognitoUUID 認証ユーザーのCognitoUUID
// * @return WebTestClient.ResponseSpec レスポンス
// */
//private fun WebTestClient.executeGraphQLRequest(
//    graphQLRequest: GraphQLRequest,
//): WebTestClient.ResponseSpec {
//    val objectMapper = ObjectMapper()
//    val requestJson = objectMapper.writeValueAsString(graphQLRequest)
//    val processedQuery = extractAndProcessQuery(requestJson)
//
//    return mutateWith(mockJwt().jwt { it.subject(cognitoUUID) }).post()
//        .uri("/graphql")
//        .accept(MediaType.APPLICATION_JSON)
//        .header("X-HMAC-Signature", hmacSignature)
//        .bodyValue(graphQLRequest)
//        .exchange()
//}
//
///**
// * JSONリクエストからクエリ部分を抽出し、処理する
// *
// * @param requestJson GraphQLRequestのJSON文字列
// * @return 処理済みのクエリ文字列
// */
//private fun extractAndProcessQuery(requestJson: String): String {
//    val regex = """"query"\s*:\s*"(.*?)"""".toRegex()
//    val match = regex.find(requestJson)
//    return match?.groupValues?.get(1)
//        ?.replace("\\n", " ")
//        ?.lines()
//        ?.joinToString(" ") { it.trim() }
//        ?.replace(Regex("\\s{2,}"), " ")
//        ?.trim()
//        ?: ""
//}
//
///**
// * リソースパスからGraphQLクエリファイルを読み込む
// *
// * @param queryPath .graphqlファイルへのリソースパス
// * @return String クエリ内容
// */
//private fun loadGraphQLQuery(queryPath: String): String {
//    val resource = ClassPathResource(queryPath)
//    return resource.inputStream.readAllBytes().toString(StandardCharsets.UTF_8)
//}
//
///**
// * GraphQLBuilderを使用してクエリを構築し、リクエストを送信する
// *
// * @param builder GraphQLBuilderを使ったクエリ構築のラムダ
// * @param secretKey HMAC署名用の秘密鍵
// * @param cognitoUUID 認証ユーザーのCognitoUUID
// * @return WebTestClient.ResponseSpec レスポンス
// */
//suspend fun WebTestClient.postGraphQLRequest(
//    builder: suspend GraphQLBuilder.() -> Unit,
//    secretKey: String = "your-secret-key",
//    cognitoUUID: String
//): WebTestClient.ResponseSpec {
//    val query = graphQLBuilder(builder)
//    return postGraphQLRequest(query, secretKey, cognitoUUID)
//}
//
///**
// * GraphQLクエリ文字列を読み込み、リクエストを送信する
// *
// * @param query 実行するGraphQLクエリ文字列
// * @param secretKey HMAC署名用の秘密鍵
// * @param cognitoUUID 認証ユーザーのCognitoUUID
// * @return WebTestClient.ResponseSpec レスポンス
// */
//fun WebTestClient.postGraphQLRequest(
//    query: String,
//    secretKey: String = "your-secret-key",
//    cognitoUUID: String
//): WebTestClient.ResponseSpec {
//    // Create the GraphQLRequest object
//    val graphQLRequest = GraphQLRequest(query)
//
//    return executeGraphQLRequest(graphQLRequest, secretKey, cognitoUUID)
//}
//
///**
// * .graphqlファイルからクエリを読み込み、リクエストを送信する
// *
// * @param queryPath .graphqlファイルへのリソースパス
// * @param variables GraphQLクエリ変数
// * @param secretKey HMAC署名用の秘密鍵
// * @param cognitoUUID 認証ユーザーのCognitoUUID
// * @return WebTestClient.ResponseSpec レスポンス
// */
//fun WebTestClient.postGraphQLRequest(
//    queryPath: String,
//    variables: Map<String, Any?> = emptyMap(),
//): WebTestClient.ResponseSpec {
//    // Create the GraphQLRequest object
//    val graphQLRequest = GraphQLRequest(
//        query = loadGraphQLQuery(queryPath),
//        variables = variables
//    )
//
//    return executeGraphQLRequest(graphQLRequest)
//}
