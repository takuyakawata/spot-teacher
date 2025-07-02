export interface AdminUser {
  id: string
  email: string
  name: string
  role: "admin" | "super_admin"
  permissions: string[]
  lastLoginAt: Date
  createdAt: Date
  updatedAt: Date
}

export interface AdminLoginRequest {
  email: string
  password: string
}

export interface AdminLoginResponse {
  user: AdminUser
  accessToken: string
  refreshToken: string
  expiresIn: number
}

export interface AdminAuthError {
  code: string
  message: string
  field?: string
}

// GraphQL Mutations
export const ADMIN_LOGIN_MUTATION = `
  mutation AdminLogin($input: AdminLoginInput!) {
    adminLogin(input: $input) {
      user {
        id
        email
        name
        role
        permissions
        lastLoginAt
        createdAt
        updatedAt
      }
      accessToken
      refreshToken
      expiresIn
    }
  }
`

export const ADMIN_REFRESH_TOKEN_MUTATION = `
  mutation AdminRefreshToken($refreshToken: String!) {
    adminRefreshToken(refreshToken: $refreshToken) {
      accessToken
      refreshToken
      expiresIn
    }
  }
`

export const ADMIN_LOGOUT_MUTATION = `
  mutation AdminLogout {
    adminLogout {
      success
    }
  }
`

// Validation schemas
import { z } from "zod"

export const adminLoginSchema = z.object({
  email: z.string().email({
    message: "有効なメールアドレスを入力してください",
  }),
  password: z.string().min(8, {
    message: "パスワードは8文字以上である必要があります",
  }),
})

export type AdminLoginFormData = z.infer<typeof adminLoginSchema>