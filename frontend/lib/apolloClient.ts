import { ApolloClient, InMemoryCache } from "@apollo/client";

export const apolloClient = new ApolloClient({
  uri: "https://spacex-production.up.railway.app/", // 必要に応じて変更
  cache: new InMemoryCache(),
}); 