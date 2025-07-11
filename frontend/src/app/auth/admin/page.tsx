"use client";
import React, { useState } from "react";
import { Card, CardHeader, CardTitle, CardContent, CardFooter, CardDescription } from "@/components/shadcn/ui/card";
import { Button } from "@/components/shadcn/ui/button";
import { Input } from "@/components/shadcn/ui/input";
import { Label } from "@/components/shadcn/ui/label";
import { useAdminLoginForm } from "./_hooks/use-admin-login-form";

export default function AdminLoginPage() {
  const { form, handleChange, handleSubmit, isLoading, error, formError } = useAdminLoginForm();

  return (
    <Card className="w-full max-w-md mx-auto min-h-screen flex flex-col justify-center shadow-lg">
      <CardHeader>
        <CardTitle>管理者ログイン</CardTitle>
        <CardDescription>管理者用ダッシュボードにログイン</CardDescription>
      </CardHeader>
      <form onSubmit={handleSubmit}>
        <CardContent className="space-y-4">
          <div>
            <Label htmlFor="email">メールアドレス</Label>
            <Input
              id="email"
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
              autoFocus
            />
          </div>
          <div>
            <Label htmlFor="password">パスワード</Label>
            <Input
              id="password"
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>
          {(formError || error) && (
            <div className="text-red-500 text-sm">
              {formError || error?.message}
            </div>
          )}
        </CardContent>
        <CardFooter>
          <Button type="submit" className="w-full" disabled={isLoading}>
            {isLoading ? "ログイン中..." : "ログイン"}
          </Button>
        </CardFooter>
      </form>
    </Card>
  );
}
