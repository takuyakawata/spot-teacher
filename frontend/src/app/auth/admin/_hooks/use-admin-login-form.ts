import { useState } from "react";
import { adminLoginSchema, AdminLoginFormData } from "../_schema/admin-auth";
import { useAdminAuth } from "./use-admin-auth";

export function useAdminLoginForm() {
  const { login, isLoading, error, clearError } = useAdminAuth();
  const [form, setForm] = useState<AdminLoginFormData>({ email: "", password: "" });
  const [formError, setFormError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setFormError(null);
    if (error) clearError();
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const result = adminLoginSchema.safeParse(form);
    if (!result.success) {
      setFormError(result.error.errors[0]?.message ?? "入力内容に誤りがあります");
      return;
    }
    await login(form);
  };

  return {
    form,
    handleChange,
    handleSubmit,
    isLoading,
    error,
    formError,
  };
} 