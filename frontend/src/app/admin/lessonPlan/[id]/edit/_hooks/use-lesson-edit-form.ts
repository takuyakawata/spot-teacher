"use client";
import { useForm, SubmitHandler } from "react-hook-form";
import { useRouter } from "next/navigation";
import React from "react";

// TODO: idやデータ取得・保存は後でGraphQLに差し替え
const dummyLesson = {
  id: "1",
  title: "算数の授業",
  description: "小学校3年生向けの算数の授業です。文章題を中心に学びます。",
  tags: ["算数", "小3"],
};

export type LessonEditFormValues = {
  title: string;
  description: string;
  tags: string;
};

export function useLessonEditForm() {
  const router = useRouter();
  const rhfForm = useForm<LessonEditFormValues>({
    defaultValues: {
      title: dummyLesson.title,
      description: dummyLesson.description,
      tags: dummyLesson.tags.join(","),
    },
  });
  const { handleSubmit, formState } = rhfForm;
  const [saving, setSaving] = React.useState(false);

  const onSubmit: SubmitHandler<LessonEditFormValues> = async (data) => {
    setSaving(true);
    // TODO: GraphQLで保存
    setTimeout(() => {
      setSaving(false);
      router.push(`/admin/lessons/${dummyLesson.id}`);
    }, 800);
  };

  const handleCancel = () => {
    router.push(`/admin/lessons/${dummyLesson.id}`);
  };

  return {
    saving,
    handleSubmit: handleSubmit(onSubmit),
    handleCancel,
    rhfForm,
  };
}