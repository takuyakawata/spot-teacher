"use client";
import { ApolloProvider as ApolloProviderBase } from "@apollo/client";
import { apolloClient } from "@/lib/apolloClient";

export const ApolloProvider = ({ children }: { children: React.ReactNode }) => (
  <ApolloProviderBase client={apolloClient}>{children}</ApolloProviderBase>
); 