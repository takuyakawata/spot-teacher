"use client"

import { useState, useCallback } from "react"
import { useRouter } from "next/navigation"
import type { AdminLoginRequest, AdminUser, AdminAuthError } from "../_schema/admin-auth"

interface UseAdminAuthReturn {
  user: AdminUser | null
  isLoading: boolean
  error: AdminAuthError | null
  login: (credentials: AdminLoginRequest) => Promise<void>
  logout: () => Promise<void>
  clearError: () => void
}

export function useAdminAuth(): UseAdminAuthReturn {
  const router = useRouter()
  const [user, setUser] = useState<AdminUser | null>(null)
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<AdminAuthError | null>(null)

  const login = useCallback(
    async (credentials: AdminLoginRequest) => {
      setIsLoading(true)
      setError(null)

      try {
        // 実際のGraphQL APIコール（現在はコメントアウト）
        /*
      const response = await fetch('/api/graphql', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          query: ADMIN_LOGIN_MUTATION,
          variables: { input: credentials }
        })
      })

      const { data, errors } = await response.json()

      if (errors) {
        throw new Error(errors[0].message)
      }

      const { user, accessToken, refreshToken } = data.adminLogin
      
      // トークンを保存
      localStorage.setItem('admin_access_token', accessToken)
      localStorage.setItem('admin_refresh_token', refreshToken)
      
      setUser(user)
      */

        // ダミー実装
        await new Promise((resolve) => setTimeout(resolve, 1000))

        // ダミーの管理者データ
        const dummyUser: AdminUser = {
          id: "1",
          email: credentials.email,
          name: "管理者",
          role: "admin",
          permissions: ["read", "write", "delete"],
          lastLoginAt: new Date(),
          createdAt: new Date(),
          updatedAt: new Date(),
        }

        // ダミートークンを保存
        localStorage.setItem("admin_access_token", "dummy_admin_token")
        localStorage.setItem("admin_refresh_token", "dummy_admin_refresh_token")

        setUser(dummyUser)
        router.push("/admin/dashboard")
      } catch (err) {
        const authError: AdminAuthError = {
          code: "LOGIN_FAILED",
          message: err instanceof Error ? err.message : "ログインに失敗しました",
        }
        setError(authError)
      } finally {
        setIsLoading(false)
      }
    },
    [router],
  )

  const logout = useCallback(async () => {
    setIsLoading(true)

    try {
      // 実際のGraphQL APIコール（現在はコメントアウト）
      /*
      await fetch('/api/graphql', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('admin_access_token')}`
        },
        body: JSON.stringify({
          query: ADMIN_LOGOUT_MUTATION
        })
      })
      */

      // トークンを削除
      localStorage.removeItem("admin_access_token")
      localStorage.removeItem("admin_refresh_token")

      setUser(null)
      router.push("/auth/admin")
    } catch (err) {
      console.error("Logout error:", err)
    } finally {
      setIsLoading(false)
    }
  }, [router])

  const clearError = useCallback(() => {
    setError(null)
  }, [])

  return {
    user,
    isLoading,
    error,
    login,
    logout,
    clearError,
  }
}
