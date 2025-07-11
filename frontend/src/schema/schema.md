全体依存のあるschemaの定義を記載
- 例
___
``` typescript
export const emailSchema = z
  .string()
  .email('有効なメールアドレスを入力してください');

export const passwordSchema = z
  .string()
  .min(8, 'パスワードは8文字以上である必要があります')
  .regex(/[a-z]/, 'パスワードは小文字を含む必要があります')
  .regex(/[0-9]/, 'パスワードは数字を含む必要があります');

export const phoneNumberSchema = z
  .string()
  .regex(/^$|^\d+$/, '電話番号は半角数字のみを入力してください')
  .max(15, '電話番号は15桁以下である必要があります');
```

