//userごとに分けたいところ

// teacher

// admin user

// company member
// import {
//   confirmResetPassword as awsConfirmResetPassword,
//   resetPassword as awsResetPassword,
//   signIn as awsSignIn,
//   signOut as awsSignOut,
//   updatePassword as awsUpdatePassword,
// } from 'aws-amplify/auth';
// MEMO: アクセストークンとリフレッシュトークンを取得できそう
// fetchAuthSession で取得

// export const signIn = async (username: string, password: string) => {
//   try {
//     const { isSignedIn, nextStep } = await awsSignIn({
//       username,
//       password,
//     });
//     return { isSignedIn, nextStep };
//   } catch (error) {
//     // biome-ignore lint/suspicious/noConsole: <explanation>
//     console.error('Error signing in:', error);
//     throw error;
//   }
// };

// export const signOut = async () => {
//   try {
//     await awsSignOut();
//   } catch (error) {
//     // biome-ignore lint/suspicious/noConsole: <explanation>
//     console.error('Error signing out:', error);
//     throw error;
//   }
// };

// export const resetPassword = async (
//   username: string,
//   code?: string,
//   newPassword?: string
// ) => {
//   try {
//     if (!code || !newPassword) {
//       // パスワードリセットの開始
//       await awsResetPassword({ username });
//       return { success: true };
//     }

//     // パスワードリセットの完了
//     await awsConfirmResetPassword({
//       username,
//       confirmationCode: code,
//       newPassword,
//     });
//     return { success: true };
//   } catch (error) {
//     // biome-ignore lint/suspicious/noConsole: <explanation>
//     console.error('Error resetting password:', error);
//     throw error;
//   }
// };

// export const updatePassword = async (
//   oldPassword: string,
//   newPassword: string
// ) => {
//   try {
//     await awsUpdatePassword({
//       oldPassword,
//       newPassword,
//     });
//     return { success: true };
//   } catch (error) {
//     // biome-ignore lint/suspicious/noConsole: <explanation>
//     console.error('Error updating password:', error);
//     throw error;
//   }
// };