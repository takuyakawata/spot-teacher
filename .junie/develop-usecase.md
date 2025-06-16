あなたは Clean Architecture + DDD を採用する Kotlin/SpringBoot プロジェクトの開発者です。
以下の制約を守りながら、新しい UseCase を実装してください。

# 1. 前提
- ドメイン: <Domain 名>        （例: Lesson, User）
- 機能概要: <処理の目的>        （例: レッスン検索, レポート作成）
- 入力パラメータ:
    * <param1>: <型> – <説明>
    * <param2>: <型> – <説明>
- 出力結果:
    * <戻り値>: <型> – <説明>
- 例外条件: <ドメイン例外一覧>

# 2. 生成対象
1. **UseCase クラス**
    - 名前: `<Action><Domain>NameUseCase` 形式
    - メソッド: `call(input: <InputDTO>): <OutputDTO>`
    - ビジネスロジックはドメインサービス／エンティティに委譲
2. **Input / Output DTO**
    - `data class <Domain><Action>Input(...)`
    - `data class <Domain><Action>Output(...)`
3. **ユニットテスト**
    - ディレクトリ: `backend/<module>/src/test/.../usecase`
    - AAA パターン、正常系 + 異常系
    - Fixture を使う(ドメイン単位で作成)
4. **Repository 依存のモック**
    - `mockk` を利用して UseCase の純度を保つ
5. **RepsitoryImplのテスト**
   - DataDescribe　testで永続化できているかを確認する
   - unitの返り値の場合、SQLかfind methodでデータの存在を確認する

# 3. コーディング規約
- 「.junie/guidelines.md」セクション 3.1 の Kotlin ベストプラクティスを厳守
