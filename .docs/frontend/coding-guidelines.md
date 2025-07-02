# コーディング規約: page コンポーネント & shadcn UI

## 1. page コンポーネントについて

- `src/app/page.tsx` などのページコンポーネントは**サーバーコンポーネント**で実装する。
- クライアント専用ロジック（useState, useEffect, framer-motion など）は**カスタムフックやクライアントコンポーネントに分離**する。
- ページ全体のラップや大枠も shadcn の`Card`や`CardContent`などで構成する。
- `div`や`p`タグは原則使わず、shadcn UI コンポーネントで表現する。
- レイアウトやセクション分割も`CardContent`や`CardHeader`、`CardFooter`などを活用する。

## 2. shadcn UI コンポーネントの利用について

- テキストの見出しや説明は`CardTitle`や`CardDescription`を使う。
- セクションやラップには`CardContent`、ヘッダーには`CardHeader`、フッターには`CardFooter`を使う。
- リストやアイコンのラップも`CardContent`や`Card`で行う。
- 可能な限り**div や p タグを使わず**、shadcn UI コンポーネントのみで構成する。
- カスタム UI や装飾も shadcn の思想に沿って`className`で調整する。

## 3. その他

- サーバーコンポーネントでクライアント専用ライブラリ（framer-motion 等）を import しない。
- UI の一貫性・保守性を重視し、shadcn UI で統一する。
- 例外的にどうしても必要な場合のみ、最小限の div や ul/li を許容する。

---

この規約に従い、プロジェクトの UI・ページ実装を行ってください。
