package com.spotteacher.admin.shared.auth.handler

//data class LoginRequest(val email: String, val password: String)
//data class AuthResponse(val accessToken: String, val refreshToken: String)
//
//@RestController
//@RequestMapping("/api/admin/auth")
//class AuthController(private val loginUse: LoginUseCase) {
//
//    @PostMapping("/login")
//    suspend fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
//        val result = loginUse.call(
//            LoginUseCaseInput(
//                EmailAddress(loginRequest.email),
//                Password(loginRequest.password)
//            )
//        )
//
//        return result.fold(
//            onSuccess = { tokenPair ->
//                ResponseEntity.ok(
//                    AuthResponse(
//                        accessToken = tokenPair.accessToken,
//                        refreshToken = tokenPair.refreshToken
//                    )
//                )
//            },
//            onFailure = {
//                ResponseEntity.status(401).build()
//            }
//        )
//}
