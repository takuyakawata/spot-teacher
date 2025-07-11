"use client";
import { motion } from "framer-motion";
import Link from "next/link";
import { ArrowDown, ArrowRight } from "lucide-react";
import { Button } from "@/components/shadcn/ui/button";
import { Card, CardContent } from "@/components/shadcn/ui/card";

export function HeroSection({ scrollY }: { scrollY: number }) {
  return (
    <Card className="relative h-screen flex items-center justify-center overflow-hidden rounded-none border-none shadow-none">
      {/* ヒーロー背景 */}
      <div
        className="absolute inset-0 bg-cover bg-center z-0"
        style={{
          backgroundImage: "url('/placeholder.svg?height=1080&width=1920')",
          filter: "brightness(0.7)",
        }}
      />
      {/* グラデーションオーバーレイ */}
      <div className="absolute inset-0 bg-gradient-to-b from-transparent to-background z-10"></div>
      {/* ヒーローコンテンツ */}
      <CardContent className="container mx-auto px-4 relative z-20 text-center flex flex-col items-center justify-center">
        <motion.div initial={{ opacity: 0, y: 30 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.8 }}>
          <h1 className="text-4xl md:text-6xl font-bold text-white mb-6">SPOT</h1>
          <p className="text-xl md:text-2xl text-white/90 mb-8 max-w-2xl mx-auto">
            教師と学校のための効率的な授業予約・管理プラットフォーム
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link href="/auth/teacher">
              <Button size="lg" className="group">
                教師としてログイン
                <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" />
              </Button>
            </Link>
            <Link href="/auth/admin">
              <Button
                size="lg"
                variant="outline"
                className="bg-white/20 backdrop-blur-sm border-white/30 text-white hover:bg-white/30 group"
              >
                管理者としてログイン
                <ArrowRight className="ml-2 h-4 w-4 transition-transform group-hover:translate-x-1" />
              </Button>
            </Link>
          </div>
        </motion.div>
      </CardContent>
      {/* スクロールインジケーター */}
      <motion.div
        className="absolute bottom-8 left-1/2 transform -translate-x-1/2 z-20"
        animate={{
          y: [0, 10, 0],
          opacity: scrollY > 100 ? 0 : 1,
        }}
        transition={{
          y: { repeat: Number.POSITIVE_INFINITY, duration: 1.5 },
          opacity: { duration: 0.3 },
        }}
      >
        <Button
          variant="ghost"
          size="icon"
          className="rounded-full border border-white/30 bg-white/10 backdrop-blur-sm text-white hover:bg-white/20"
        >
          <ArrowDown className="h-5 w-5" />
          <span className="sr-only">スクロールして詳細を見る</span>
        </Button>
      </motion.div>
    </Card>
  );
} 