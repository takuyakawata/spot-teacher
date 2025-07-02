あなたは Clean Architecture + DDD を採用する Kotlin/SpringBoot プロジェクトの AI 開発者です。
以下の制約を守りながら、新しい Repository を実装してください。

# 1. 前提
- ドメイン: <Domain 名>                       （例: Lesson）
- エンティティ: <Entity 名>                   （例: LessonEntity）
- データソース: <RDB テーブル or 外部 API>      （例: lessons テーブル）
- 主要メソッド:
    * <method1>(<args>): <戻り値> – <説明>
- トランザクション境界: <必要なら説明>

# 2. 生成対象
1. **Repository インターフェース**（Domain 層）
    - パッケージ: `com.cosoji.backend.<module>.domain.repository`
    - 命名: `<Entity>Repository`
2. **Repository 実装**（Infra 層）
    - パッケージ: `com.cosoji.backend.<module>.infra.repository`
    - 名前: `<Entity>RepositoryImpl`
    - jOOQ または Spring JDBC を使用（既存実装に合わせる）
    - DTO ↔ Entity 変換は Mapper クラスに委譲
3. **Infra 単体テスト**
    - ディレクトリ: `backend/<module>/src/test/.../infra`
    - Testcontainers で DB を立ち上げて実装を検証
    - 主要メソッドの正常系 + 異常系
4. **Domain fixtureを仕様して生成**
    - 　`com.cosoji.backend.<module>.domain.fixture` パッケージに配置
    - `<Entity>Fixture` クラスを作成
    - 　すでに存在する場合は、使用する

# 3. コーディング規約
- `.junie/guidelines.md` セクション 3.1 を厳守（val 優先・null 安全 等）
