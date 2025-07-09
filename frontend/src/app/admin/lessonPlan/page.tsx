"use client";
import React from "react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/shadcn/ui/card";
import { Button } from "@/components/shadcn/ui/button";
import { Badge } from "@/components/shadcn/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/shadcn/ui/table";

// TODO: GraphQLクエリに差し替え
const dummyLessons = [
  { id: "1", title: "算数の授業", description: "小学校3年生向けの算数", createdAt: "2024-06-01", tags: ["算数", "小3"] },
  { id: "2", title: "英語の授業", description: "中学校1年生向けの英語", createdAt: "2024-06-02", tags: ["英語", "中1"] },
];

export default function LessonsPage() {
  return (
    <Card>
      <CardHeader>
        <CardTitle>レッスン一覧</CardTitle>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>タイトル</TableHead>
              <TableHead>説明</TableHead>
              <TableHead>作成日</TableHead>
              <TableHead>タグ</TableHead>
              <TableHead>操作</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {dummyLessons.map(lesson => (
              <TableRow key={lesson.id}>
                <TableCell>{lesson.title}</TableCell>
                <TableCell>{lesson.description}</TableCell>
                <TableCell>{lesson.createdAt}</TableCell>
                <TableCell>
                  <div className="flex flex-wrap gap-1">
                    {lesson.tags.map(tag => (
                      <Badge key={tag} variant="secondary">{tag}</Badge>
                    ))}
                  </div>
                </TableCell>
                <TableCell className="space-x-2">
                  <Button size="sm" variant="outline" onClick={() => window.location.href = `/admin/lessons/${lesson.id}`}>詳細</Button>
                  <Button size="sm" variant="secondary" onClick={() => window.location.href = `/admin/lessons/${lesson.id}/edit`}>編集</Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
}
