import Link from "next/link"
import { ArrowDown, ArrowRight, School, User } from "lucide-react"

import { Button } from "@/components/shadcn/ui/button"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/shadcn/ui/card"
import { Footer } from "@/components/footer"
import { AnimatedSection } from "@/components/animated-section"

export default function HomePage() {
  return (
    <Card className="min-h-screen flex flex-col">
      {/* メインコンテンツ */}
      <main className="flex-1 container mx-auto px-4 py-24">
        <AnimatedSection>
          <CardContent className="max-w-4xl mx-auto">
            <Card className="mb-16">
              <CardHeader className="text-center">
                <CardTitle>授業予約・管理システム</CardTitle>
                <CardDescription>簡単に授業を予約し、効率的に管理できるプラットフォーム</CardDescription>
              </CardHeader>
            </Card>
            <CardContent className="grid md:grid-cols-2 gap-8 mb-16">
              <Card>
                <CardContent className="h-40 bg-gradient-to-r from-blue-500 to-cyan-400 relative overflow-hidden">
                  <CardContent className="absolute inset-0 flex items-center justify-center text-white/20">
                    <User className="w-32 h-32" strokeWidth={1} />
                  </CardContent>
                </CardContent>
                <CardHeader>
                  <CardTitle className="flex items-center">
                    <User className="mr-2 h-5 w-5" />
                    教師の方
                  </CardTitle>
                  <CardDescription>授業の予約、確認、報告書の提出などを行えます</CardDescription>
                </CardHeader>
                <CardContent>
                  <CardDescription>
                    <ul className="list-disc pl-5 space-y-2">
                      <li>授業の検索と予約</li>
                      <li>予約履歴の確認</li>
                      <li>授業実施の確認</li>
                      <li>報告書の作成と提出</li>
                    </ul>
                  </CardDescription>
                </CardContent>
                <CardFooter>
                  <Link href="/auth/teacher" className="w-full">
                    <Button className="w-full group">
                      教師としてログイン
                      <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" />
                    </Button>
                  </Link>
                </CardFooter>
              </Card>
              <Card>
                <CardContent className="h-40 bg-gradient-to-r from-indigo-500 to-purple-400 relative overflow-hidden">
                  <CardContent className="absolute inset-0 flex items-center justify-center text-white/20">
                    <School className="w-32 h-32" strokeWidth={1} />
                  </CardContent>
                </CardContent>
                <CardHeader>
                  <CardTitle className="flex items-center">
                    <School className="mr-2 h-5 w-5" />
                    管理者の方
                  </CardTitle>
                  <CardDescription>授業の作成、予約の管理、報告書の確認などを行えます</CardDescription>
                </CardHeader>
                <CardContent>
                  <CardDescription>
                    <ul className="list-disc pl-5 space-y-2">
                      <li>授業の作成と管理</li>
                      <li>予約状況の確認</li>
                      <li>授業実施の管理</li>
                      <li>報告書の確認と統計</li>
                    </ul>
                  </CardDescription>
                </CardContent>
                <CardFooter>
                  <Link href="/auth/admin" className="w-full">
                    <Button className="w-full" variant="outline">
                      管理者としてログイン
                      <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" />
                    </Button>
                  </Link>
                </CardFooter>
              </Card>
            </CardContent>
            <CardContent className="text-center">
              <Card className="p-8">
                <CardHeader>
                  <CardTitle>システムの特徴</CardTitle>
                </CardHeader>
                <CardContent>
                  <CardContent className="grid sm:grid-cols-3 gap-8">
                    <Card className="p-6 rounded-lg bg-gradient-to-br from-background to-muted border border-border transition-all duration-300 hover:shadow-md">
                      <CardContent className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mx-auto mb-4">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="24"
                          height="24"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          className="text-primary"
                        >
                          <rect width="18" height="18" x="3" y="3" rx="2" />
                          <path d="M7 7h10" />
                          <path d="M7 12h10" />
                          <path d="M7 17h10" />
                        </svg>
                      </CardContent>
                      <CardTitle className="font-medium text-lg mb-2">簡単な予約管理</CardTitle>
                      <CardDescription className="text-sm">直感的なインターフェースで授業の予約と管理が簡単に行えます</CardDescription>
                    </Card>
                    <Card className="p-6 rounded-lg bg-gradient-to-br from-background to-muted border border-border transition-all duration-300 hover:shadow-md">
                      <CardContent className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mx-auto mb-4">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="24"
                          height="24"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          className="text-primary"
                        >
                          <path d="M14 3v4a1 1 0 0 0 1 1h4" />
                          <path d="M17 21H7a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h7l5 5v11a2 2 0 0 1-2 2z" />
                          <path d="M9 9h1" />
                          <path d="M9 13h6" />
                          <path d="M9 17h6" />
                        </svg>
                      </CardContent>
                      <CardTitle className="font-medium text-lg mb-2">効率的な報告</CardTitle>
                      <CardDescription className="text-sm">授業後の報告書作成と提出がスムーズに行えます</CardDescription>
                    </Card>
                    <Card className="p-6 rounded-lg bg-gradient-to-br from-background to-muted border border-border transition-all duration-300 hover:shadow-md">
                      <CardContent className="w-12 h-12 rounded-full bg-primary/10 flex items-center justify-center mx-auto mb-4">
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          width="24"
                          height="24"
                          viewBox="0 0 24 24"
                          fill="none"
                          stroke="currentColor"
                          strokeWidth="2"
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          className="text-primary"
                        >
                          <path d="M3 3v18h18" />
                          <path d="m19 9-5 5-4-4-3 3" />
                        </svg>
                      </CardContent>
                      <CardTitle className="font-medium text-lg mb-2">データの可視化</CardTitle>
                      <CardDescription className="text-sm">授業データを分析し、効果的な意思決定をサポートします</CardDescription>
                    </Card>
                  </CardContent>
                </CardContent>
              </Card>
            </CardContent>
          </CardContent>
        </AnimatedSection>
      </main>

      <Footer />
    </Card>
  )
}