"use client";
import { ApolloProvider as Provider } from "@apollo/client";
import { apolloClient } from "@/lib/apolloClient";

export const ApolloProvider = ({ children }: { children: React.ReactNode }) => (
  <Provider client={apolloClient}>{children}</Provider>
); 