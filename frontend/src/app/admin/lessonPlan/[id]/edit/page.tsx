import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/shadcn/ui/card";
import { Input } from "@/components/shadcn/ui/input";
import { Button } from "@/components/shadcn/ui/button";
import { Badge } from "@/components/shadcn/ui/badge";
import { useLessonEditForm, LessonEditFormValues } from "./_hooks/use-lesson-edit-form";
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/components/shadcn/ui/form";

export default function LessonEditPage() {
  const { saving, handleSubmit, handleCancel, rhfForm } = useLessonEditForm();

  return (
    <Card className="max-w-xl mx-auto mt-10">
      <CardHeader>
        <CardTitle>レッスン編集</CardTitle>
      </CardHeader>
      <Form form={rhfForm} onSubmit={handleSubmit}>
        <CardContent className="space-y-4">
          <FormField name="title">
            {({ field }: { field: any }) => (
              <FormItem>
                <FormLabel>タイトル</FormLabel>
                <FormControl>
                  <Input {...field} required />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          </FormField>
          <FormField name="description">
            {({ field }: { field: any }) => (
              <FormItem>
                <FormLabel>説明</FormLabel>
                <FormControl>
                  <textarea {...field} required className="flex h-20 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50" />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          </FormField>
          <FormField name="tags">
            {({ field }: { field: any }) => (
              <FormItem>
                <FormLabel>タグ（カンマ区切り）</FormLabel>
                <FormControl>
                  <Input {...field} placeholder="例: 算数,小3" />
                </FormControl>
                {(field.value as string).split(",").filter(Boolean).map((tag: string) => (
                  <Badge key={tag.trim()} variant="secondary" className="mr-1 mt-2 inline-block">{tag.trim()}</Badge>
                ))}
                <FormMessage />
              </FormItem>
            )}
          </FormField>
        </CardContent>
        <CardFooter className="space-x-2">
          <Button type="submit" disabled={saving}>
            {saving ? "保存中..." : "保存"}
          </Button>
          <Button type="button" variant="outline" onClick={handleCancel}>
            キャンセル
          </Button>
        </CardFooter>
      </Form>
    </Card>
  );
}
