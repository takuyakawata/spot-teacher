"use client";
import { Card, CardHeader, CardTitle, CardContent, CardDescription, CardFooter } from "@/components/shadcn/ui/card";
import { Badge } from "@/components/shadcn/ui/badge";
import { Button } from "@/components/shadcn/ui/button";
import { useRouter } from "next/navigation";

// TODO: idやデータ取得は後でGraphQLに差し替え
const dummyLesson = {
  id: "1",
  title: "算数の授業",
  description: "小学校3年生向けの算数の授業です。文章題を中心に学びます。",
  createdAt: "2024-06-01",
  tags: ["算数", "小3"],
};

export default function LessonDetailPage() {
  const router = useRouter();
  const lesson = dummyLesson; // TODO: idで切り替え

  return (
    <Card className="max-w-xl mx-auto mt-10">
      <CardHeader>
        <CardTitle>{lesson.title}</CardTitle>
        <CardDescription>作成日: {lesson.createdAt}</CardDescription>
      </CardHeader>
      <CardContent className="space-y-4">
        <div>
          <span className="font-semibold">説明：</span>
          <span>{lesson.description}</span>
        </div>
        <div className="flex gap-2">
          {lesson.tags.map(tag => (
            <Badge key={tag} variant="secondary">{tag}</Badge>
          ))}
        </div>
      </CardContent>
      <CardFooter className="space-x-2">
        <Button variant="secondary" onClick={() => router.push(`/admin/lessons/${lesson.id}/edit`)}>編集</Button>
        <Button variant="outline" onClick={() => router.push(`/admin/lessons`)}>一覧へ戻る</Button>
      </CardFooter>
    </Card>
  );
} 