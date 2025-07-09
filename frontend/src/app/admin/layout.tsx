import { ApolloProvider } from "../../provider/ApolloProvider";

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return <ApolloProvider>{children}</ApolloProvider>;
}
