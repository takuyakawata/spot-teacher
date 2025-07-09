"use client";
import React from "react";
import { Card, CardHeader, CardTitle, CardContent, CardDescription, CardFooter } from "@/components/shadcn/ui/card";
import { Button } from "@/components/shadcn/ui/button";
import { useGetSchoolQuery, useUpdateSchoolMutation, useDeleteSchoolMutation } from "@/gql/graphql";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function Page({ params }: { params: { id: string } }) {
  const router = useRouter();
  const { data, loading, error } = useGetSchoolQuery({ variables: { id: params.id } });
  const [updateSchool, { loading: updating }] = useUpdateSchoolMutation();
  const [deleteSchool, { loading: deleting }] = useDeleteSchoolMutation();
  const [editMode, setEditMode] = useState(false);
  const [form, setForm] = useState({
    name: data?.school?.name ?? "",
    schoolCategory: data?.school?.schoolCategory ?? "ELEMENTARY",
    city: data?.school?.city ?? "",
    streetAddress: data?.school?.streetAddress ?? "",
    prefecture: data?.school?.prefecture ?? "",
    postalCode: data?.school?.postalCode ?? "",
    phoneNumber: data?.school?.phoneNumber ?? "",
    url: data?.school?.url ?? "",
    buildingName: data?.school?.buildingName ?? "",
  });

  // データ取得後にformを初期化
  React.useEffect(() => {
    if (data?.school) {
      setForm({
        name: data.school.name ?? "",
        schoolCategory: data.school.schoolCategory ?? "ELEMENTARY",
        city: data.school.city ?? "",
        streetAddress: data.school.streetAddress ?? "",
        prefecture: data.school.prefecture ?? "",
        postalCode: data.school.postalCode ?? "",
        phoneNumber: data.school.phoneNumber ?? "",
        url: data.school.url ?? "",
        buildingName: data.school.buildingName ?? "",
      });
    }
  }, [data?.school]);

  if (loading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>学校詳細</CardTitle>
        </CardHeader>
        <CardContent>
          <CardDescription>読み込み中...</CardDescription>
        </CardContent>
      </Card>
    );
  }
  if (error || !data?.school) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>学校詳細</CardTitle>
        </CardHeader>
        <CardContent>
          <CardDescription>データ取得エラー: {error?.message ?? "学校が見つかりません"}</CardDescription>
        </CardContent>
      </Card>
    );
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleUpdate = async () => {
    await updateSchool({
      variables: {
        input: {
          id: params.id,
          ...form,
        },
      },
    });
    setEditMode(false);
  };

  const handleDelete = async () => {
    if (confirm("本当に削除しますか？")) {
      await deleteSchool({ variables: { input: { id: params.id } } });
      router.push("/admin/schools");
    }
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>学校詳細</CardTitle>
        <CardDescription>ID: {data.school.id}</CardDescription>
      </CardHeader>
      <CardContent>
        {editMode ? (
          <form className="space-y-2" onSubmit={e => { e.preventDefault(); handleUpdate(); }}>
            <CardDescription>学校名</CardDescription>
            <input name="name" value={form.name} onChange={handleChange} className="border rounded px-2 py-1 w-full" required />
            <CardDescription>カテゴリ</CardDescription>
            <select name="schoolCategory" value={form.schoolCategory} onChange={handleChange} className="border rounded px-2 py-1 w-full">
              <option value="ELEMENTARY">小学校</option>
              <option value="JUNIOR_HIGH">中学校</option>
              <option value="HIGH">高校</option>
            </select>
            <CardDescription>都道府県</CardDescription>
            <input name="prefecture" value={form.prefecture} onChange={handleChange} className="border rounded px-2 py-1 w-full" required />
            <CardDescription>市区町村</CardDescription>
            <input name="city" value={form.city} onChange={handleChange} className="border rounded px-2 py-1 w-full" required />
            <CardDescription>番地</CardDescription>
            <input name="streetAddress" value={form.streetAddress} onChange={handleChange} className="border rounded px-2 py-1 w-full" required />
            <CardDescription>建物名</CardDescription>
            <input name="buildingName" value={form.buildingName} onChange={handleChange} className="border rounded px-2 py-1 w-full" />
            <CardDescription>郵便番号</CardDescription>
            <input name="postalCode" value={form.postalCode} onChange={handleChange} className="border rounded px-2 py-1 w-full" required />
            <CardDescription>電話番号</CardDescription>
            <input name="phoneNumber" value={form.phoneNumber} onChange={handleChange} className="border rounded px-2 py-1 w-full" />
            <CardDescription>URL</CardDescription>
            <input name="url" value={form.url} onChange={handleChange} className="border rounded px-2 py-1 w-full" />
            <CardFooter className="gap-2">
              <Button type="submit" disabled={updating}>保存</Button>
              <Button type="button" variant="secondary" onClick={() => setEditMode(false)}>キャンセル</Button>
            </CardFooter>
          </form>
        ) : (
          <>
            <CardDescription>学校名: {data.school.name}</CardDescription>
            <CardDescription>カテゴリ: {data.school.schoolCategory}</CardDescription>
            <CardDescription>都道府県: {data.school.prefecture}</CardDescription>
            <CardDescription>市区町村: {data.school.city}</CardDescription>
            <CardDescription>番地: {data.school.streetAddress}</CardDescription>
            <CardDescription>建物名: {data.school.buildingName ?? "-"}</CardDescription>
            <CardDescription>郵便番号: {data.school.postalCode}</CardDescription>
            <CardDescription>電話番号: {data.school.phoneNumber ?? "-"}</CardDescription>
            <CardDescription>URL: {data.school.url ?? "-"}</CardDescription>
          </>
        )}
      </CardContent>
      <CardFooter className="gap-2">
        {editMode ? null : (
          <>
            <Button onClick={() => setEditMode(true)} variant="default">編集</Button>
            <Button onClick={handleDelete} variant="destructive" disabled={deleting}>削除</Button>
            <Button onClick={() => router.push("/admin/schools")} variant="secondary">一覧へ戻る</Button>
          </>
        )}
      </CardFooter>
    </Card>
  );
}
