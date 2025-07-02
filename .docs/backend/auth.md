
認証の流れ
``` mermaid
sequenceDiagram
participant Client as クライアント
participant AuthController as AuthController<br>(/api/auth/login)
participant AuthManager as ReactiveAuthenticationManager<br>(Spring Security)
participant UserDetailsSvc as UserDetailsServiceImpl
participant JwtProvider as JwtProvider
participant RefreshTokenSvc as RefreshTokenService


    Client->>AuthController: 1. /loginリクエスト (email, password)
    AuthController->>AuthManager: 2. authenticate(token)
    Note right of AuthManager: ユーザー認証を依頼
    
    AuthManager->>UserDetailsSvc: 3. findByUsername(email)
    Note right of UserDetailsSvc: ユーザー情報をDBから取得
    UserDetailsSvc-->>AuthManager: 4. Mono<UserDetails> (ハッシュ化済パスワード等)
    
    Note over AuthManager: 5. 受信したパスワードとDBのパスワードを<br/>PasswordEncoderで比較・検証
    
    AuthManager-->>AuthController: 6. Mono<Authentication> (認証成功)
    
    AuthController->>JwtProvider: 7. createAccessToken(auth)
    JwtProvider-->>AuthController: 8. AccessToken (文字列)
    
    AuthController->>RefreshTokenSvc: 9. createAndSaveRefreshToken(user)
    RefreshTokenSvc-->>AuthController: 10. RefreshToken (文字列)
    
    AuthController-->>Client: 11. レスポンス (AccessToken, RefreshToken)
```
