'use client';
import { Card, CardHeader, CardTitle, CardContent, CardDescription } from "@/components/shadcn/ui/card";
import { useGetSchoolsQuery } from "@/gql/graphql";

export default function Page() {
  const { data, loading, error } = useGetSchoolsQuery();

  if (loading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>学校一覧</CardTitle>
        </CardHeader>
        <CardContent>
          <CardDescription>読み込み中...</CardDescription>
        </CardContent>
      </Card>
    );
  }
  if (error) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>学校一覧</CardTitle>
        </CardHeader>
        <CardContent>
          <CardDescription>エラーが発生しました: {error.message}</CardDescription>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle>学校一覧</CardTitle>
        <CardDescription>登録されている学校のリストです</CardDescription>
      </CardHeader>
      <CardContent>
        {data?.schools.length === 0 ? (
          <CardDescription>学校が登録されていません</CardDescription>
        ) : (
          data?.schools.map((school) => (
            <Card key={school.id} className="mb-4">
              <CardHeader>
                <CardTitle>{school.name}</CardTitle>
                <CardDescription>{school.schoolCategory} / {school.prefecture} {school.city}</CardDescription>
              </CardHeader>
              <CardContent>
                <CardDescription>住所: {school.prefecture}{school.city}{school.streetAddress}{school.buildingName ? ` ${school.buildingName}` : ""}</CardDescription>
                <CardDescription>電話番号: {school.phoneNumber ?? "-"}</CardDescription>
                <CardDescription>URL: {school.url ?? "-"}</CardDescription>
              </CardContent>
            </Card>
          ))
        )}
      </CardContent>
    </Card>
  );
}
