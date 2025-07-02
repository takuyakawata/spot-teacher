- プロダクトの構成

- app router の url 戦略

```
app/
├── (public)/          # ゲスト向けページのグループ (URLに影響しない)
│   ├── page.tsx       # トップページ (/)
│   ├── schools/
│   │   ├── page.tsx   # 学校一覧 (/schools)
│   │   └── [id]/
│   │       └── page.tsx # 学校詳細 (/schools/[id])
│   ├── lessons/
│   │   ├── page.tsx   # 授業計画一覧 (/lessons)
│   │   └── [id]/
│   │       └── page.tsx # 授業計画詳細 (/lessons/[id])
│   └── contact/
│       └── page.tsx   # お問い合わせ (/contact)
├── (auth)/            # 認証関連ページのグループ (URLに影響しない)
│   └── login/
│       └── page.tsx   # ログイン (/login)
├── teacher/           # 先生向け
│   ├── layout.tsx     # 先生向けレイアウト
│   ├── dashboard/
│   │   └── page.tsx   # 先生向けダッシュボード (/teacher/dashboard)
│   ├── reservations/
│   │   ├── page.tsx   # 予約一覧 (/teacher/reservations)
│   │   ├── create/
│   │   │   └── page.tsx # 予約作成 (/teacher/reservations/create)
│   │   └── [id]/
│   │       └── page.tsx # 特定の予約詳細 (/teacher/reservations/[id])
│   ├── history/
│   │   └── page.tsx   # 授業履歴 (/teacher/history)
│   └── settings/
│       └── page.tsx   # 設定 (/teacher/settings)
├── company/           # 会社メンバー向け
│   ├── layout.tsx     # 会社メンバー向けレイアウト
│   ├── dashboard/
│   │   └── page.tsx   # 会社メンバー向けダッシュボード (/company/dashboard)
│   ├── lessons/
│   │   ├── page.tsx   # 授業計画一覧 (/company/lessons)
│   │   ├── create/
│   │   │   └── page.tsx # 授業計画作成 (/company/lessons/create)
│   │   └── [id]/
│   │       ├── page.tsx # 詳細 (/company/lessons/[id])
│   │       └── edit/
│   │           └── page.tsx # 編集 (/company/lessons/[id]/edit)
│   ├── confirmations/
│   │   └── page.tsx   # 実施確認 (/company/confirmations)
│   ├── reports/
│   │   └── create/
│   │       └── page.tsx # 報告作成 (/company/reports/create)
│   └── history/
│       └── page.tsx   # 授業履歴 (/company/history)
├── admin/             # 事務局向け
│   ├── layout.tsx     # 事務局向けレイアウト
│   ├── dashboard/
│   │   └── page.tsx   # 事務局向けダッシュボード (/admin/dashboard)
│   ├── users/
│   │   ├── page.tsx   # ユーザー管理一覧 (/admin/users)
│   │   ├── create/
│   │   │   └── page.tsx # ユーザー作成 (/admin/users/create)
│   │   └── [id]/
│   │       ├── page.tsx # 詳細/編集 (/admin/users/[id])
│   │       └── history/
│   │           └── page.tsx # ユーザー履歴 (/admin/users/[id]/history)
│   ├── companies/
│   │   ├── page.tsx   # 会社管理一覧 (/admin/companies)
│   │   └── [id]/
│   │       └── page.tsx # 詳細/編集 (/admin/companies/[id])
│   ├── schools/
│   │   ├── page.tsx   # 学校管理一覧 (/admin/schools)
│   │   └── [id]/
│   │       ├── page.tsx # 詳細/編集 (/admin/schools/[id])
│   │       └── history/
│   │           └── page.tsx # 学校履歴 (/admin/schools/[id]/history)
│   ├── lessons/
│   │   ├── page.tsx   # 授業計画一覧 (/admin/lessons)
│   │   └── [id]/
│   │       └── edit/
│   │           └── page.tsx # 編集 (/admin/lessons/[id]/edit)
│   ├── reservations/
│   │   └── page.tsx   # 予約管理 (/admin/reservations)
│   ├── confirmations/
│   │   └── page.tsx   # 実施確定管理 (/admin/confirmations)
│   ├── reports/
│   │   └── page.tsx   # 報告管理 (/admin/reports)
│   ├── questionnaires/
│   │   └── page.tsx   # アンケート管理 (/admin/questionnaires)
│   └── settings/
│       └── page.tsx   # 設定 (/admin/settings)
└── layout.tsx         # 全体レイアウト

```

認可の戦略がたてやすい
layout で一括管理できるようにする


