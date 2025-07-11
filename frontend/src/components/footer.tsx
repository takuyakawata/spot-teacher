import { Card, CardContent } from "@/components/shadcn/ui/card";

export function Footer() {
  return (
    <footer className="bg-muted py-8">
      <Card className="container mx-auto px-4 text-center bg-muted border-none shadow-none">
        <CardContent>
          <span className="text-sm text-muted-foreground">
            &copy; {new Date().getFullYear()} SPOT - 授業予約・管理システム. All rights reserved.
          </span>
        </CardContent>
      </Card>
    </footer>
  );
} 