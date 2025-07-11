"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/shadcn/ui/card"
import { ChartContainer, ChartTooltip, ChartTooltipContent } from "@/components/shadcn/ui/chart"
import { Button } from "@/components/shadcn/ui/button"
import { Badge } from "@/components/shadcn/ui/badge"
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, ResponsiveContainer, LineChart, Line, PieChart, Pie, Cell } from "recharts"

const monthlyData = [
  { month: "Jan", users: 65, lessons: 120, revenue: 2400 },
  { month: "Feb", users: 78, lessons: 135, revenue: 2700 },
  { month: "Mar", users: 90, lessons: 160, revenue: 3200 },
  { month: "Apr", users: 95, lessons: 180, revenue: 3600 },
  { month: "May", users: 110, lessons: 200, revenue: 4000 },
  { month: "Jun", users: 125, lessons: 220, revenue: 4400 },
]

const lessonTypeData = [
  { name: "英会話", value: 45, color: "#8884d8" },
  { name: "数学", value: 30, color: "#82ca9d" },
  { name: "国語", value: 15, color: "#ffc658" },
  { name: "その他", value: 10, color: "#ff7c7c" },
]

const chartConfig = {
  users: {
    label: "ユーザー数",
  },
  lessons: {
    label: "レッスン数",
  },
  revenue: {
    label: "売上",
  },
}

export default function DashboardPage() {
  return (
    <Card className="p-6 space-y-6 border-none shadow-none">
      <CardHeader className="flex flex-row items-center justify-between px-0">
        <CardContent className="p-0">
          <CardTitle className="text-3xl font-bold">ダッシュボード</CardTitle>
          <CardDescription>システム全体の概要をご確認いただけます</CardDescription>
        </CardContent>
        <Button>レポート出力</Button>
      </CardHeader>

      <CardContent className="grid gap-4 md:grid-cols-2 lg:grid-cols-4 px-0">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">総ユーザー数</CardTitle>
          </CardHeader>
          <CardContent>
            <CardTitle className="text-2xl font-bold">1,234</CardTitle>
            <Badge variant="secondary" className="mt-2">
              +12% from last month
            </Badge>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">今月のレッスン数</CardTitle>
          </CardHeader>
          <CardContent>
            <CardTitle className="text-2xl font-bold">856</CardTitle>
            <Badge variant="secondary" className="mt-2">
              +8% from last month
            </Badge>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">売上</CardTitle>
          </CardHeader>
          <CardContent>
            <CardTitle className="text-2xl font-bold">¥4,400,000</CardTitle>
            <Badge variant="secondary" className="mt-2">
              +15% from last month
            </Badge>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">アクティブ講師数</CardTitle>
          </CardHeader>
          <CardContent>
            <CardTitle className="text-2xl font-bold">89</CardTitle>
            <Badge variant="secondary" className="mt-2">
              +3% from last month
            </Badge>
          </CardContent>
        </Card>
      </CardContent>

      <CardContent className="grid gap-4 md:grid-cols-2 px-0">
        <Card>
          <CardHeader>
            <CardTitle>月別推移</CardTitle>
            <CardDescription>ユーザー数とレッスン数の推移</CardDescription>
          </CardHeader>
          <CardContent>
            <ChartContainer config={chartConfig} className="h-[300px]">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={monthlyData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <ChartTooltip content={<ChartTooltipContent />} />
                  <Line 
                    type="monotone" 
                    dataKey="users" 
                    stroke="hsl(var(--primary))" 
                    strokeWidth={2}
                    name="ユーザー数"
                  />
                  <Line 
                    type="monotone" 
                    dataKey="lessons" 
                    stroke="hsl(var(--secondary))" 
                    strokeWidth={2}
                    name="レッスン数"
                  />
                </LineChart>
              </ResponsiveContainer>
            </ChartContainer>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>レッスン種別</CardTitle>
            <CardDescription>レッスンタイプ別の割合</CardDescription>
          </CardHeader>
          <CardContent>
            <ChartContainer config={chartConfig} className="h-[300px]">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={lessonTypeData}
                    cx="50%"
                    cy="50%"
                    outerRadius={80}
                    dataKey="value"
                    nameKey="name"
                  >
                    {lessonTypeData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                  <ChartTooltip content={<ChartTooltipContent />} />
                </PieChart>
              </ResponsiveContainer>
            </ChartContainer>
          </CardContent>
        </Card>
      </CardContent>

      <Card>
        <CardHeader>
          <CardTitle>月別売上</CardTitle>
          <CardDescription>過去6ヶ月の売上推移</CardDescription>
        </CardHeader>
        <CardContent>
          <ChartContainer config={chartConfig} className="h-[300px]">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={monthlyData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis />
                <ChartTooltip content={<ChartTooltipContent />} />
                <Bar 
                  dataKey="revenue" 
                  fill="hsl(var(--primary))" 
                  name="売上 (¥)"
                />
              </BarChart>
            </ResponsiveContainer>
          </ChartContainer>
        </CardContent>
      </Card>
    </Card>
  )
}
