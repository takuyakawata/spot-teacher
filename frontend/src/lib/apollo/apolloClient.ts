// import { ApolloLink, HttpLink } from '@apollo/client';
// import {
//   ApolloClient,
//   InMemoryCache,
//   SSRMultipartLink,
// } from '@apollo/client-integration-nextjs';
// import { setContext } from '@apollo/client/link/context';

// // API RouteへのHttpLinkを作成
// const httpLink = new HttpLink({ uri: '/api/graphql' });

// // 認証ヘッダーを追加するリンク (setContextを使用)
// // クライアントサイドからのリクエストにAmplifyの認証トークンを付与する
// const authLink = setContext(async (_, { headers }) => {
//   try {
//     // クライアントサイドでのみ実行されるようにする
//     if (typeof window !== 'undefined') {
//       // クライアントサイドでのみ実行されるようにする
//       const session = await fetchAuthSession();
//       const accessToken = session.tokens?.accessToken?.toString();
//       if (accessToken) {
//         return {
//           headers: {
//             ...headers,
//             Authorization: `Bearer ${accessToken}`,
//           },
//         };
//       }
//     }
//   } catch (error) {
//     // biome-ignore lint/suspicious/noConsole: <explanation>
//     console.error('Error fetching auth session:', error);
//     // エラーが発生してもリクエストは続行させる
//   }
//   // トークンがない場合やサーバーサイドでは元のヘッダーを返す
//   return { headers };
// });

// // Apollo Clientインスタンスを作成する関数
// // Next.js App Router (SSR/RSC) 環境に対応
// export function makeClient() {
//   // Use standard ApolloClient and InMemoryCache
//   return new ApolloClient({
//     cache: new InMemoryCache(),
//     defaultOptions: {
//       watchQuery: {
//         fetchPolicy: 'cache-and-network', // まずキャッシュを見て、データがあればそれを表示しつつ、裏側でサーバーに新しいデータがないか確認しに行く
//       },
//     },
//     link:
//       typeof window === 'undefined'
//         ? ApolloLink.from([
//             // Server: SSRMultipartLink -> httpLink
//             // サーバーサイドレンダリング時に必要
//             new SSRMultipartLink({
//               stripDefer: true,
//             }),
//             httpLink, // API Routeへのリンク
//           ])
//         : ApolloLink.from([
//             // Client: authLink -> httpLink
//             authLink, // 認証ヘッダーを追加
//             httpLink, // API Routeへのリンク
//           ]),
//   });
// }

