# Project Guidelines
    
This is a placeholder of the project guidelines for Junie.
Replace this text with any project-level instructions for Junie, e.g.:

* What is the project structure
* Whether Junie should run tests to check the correctness of the proposed solution
* How does Junie run tests (once it requires any non-standard approach)
* Whether Junie should build the project before submitting the result
* Any code-style related instructions

As an option you can ask Junie to create these guidelines for you.


* **`val` を優先し `var` は極力使わない** — 不変データをデフォルト化する
* **やりすぎない** — 可読性を損なう複雑なスコープ関数チェーンは避ける
* **シンプルな null チェック** は `isNullOrEmpty()` など標準関数を利用。エルビス演算子 `?:` を活用する
* ドメイン仕様で *絶対に* `null` が来ない箇所のみ `!!` を許容する
* **引数が 3 つ以上** の関数呼び出しは以下のように改行して整列する
  ```kotlin
  doSomething(
      id = userId,
      name = userName,
      age = userAge
  )
  ```
* **引数が多い場合、data classを活用する
* 
* **スコープ関数** は `apply` / `let` を中心に、意味を理解して使う。
* 可読性が落ちる場合は普通の `if` / `when` に戻す。

* **リスト操作** では `NonEmptyList` → 空リスト禁止を明示。Nel型を活用する。
* Nel<UserId> ,List<UserId>
    * 大規模な含有チェックは `Set` 片方に変換し O(1) 参照で最適化。
