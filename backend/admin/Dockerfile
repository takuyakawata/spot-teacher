## amazon corretto 21(依存関係のキャッシュ)
FROM amazoncorretto:21 AS dependencies
## 依存関係の解決に必要なファイルのみをコピー
COPY build.gradle.kts settings.gradle.kts gradlew /usr/src/app/
COPY gradle/ /usr/src/app/gradle/
## ワーキングディレクトリを指定
WORKDIR /usr/src/app
## 依存関係をダウンロード
RUN ./gradlew dependencies --no-daemon

## amazon corretto 21(ビルド)
FROM amazoncorretto:21 AS builder
## 依存関係のキャッシュをコピー
COPY --from=dependencies /root/.gradle /root/.gradle
## ソースをコピーしてビルド
COPY build.gradle.kts settings.gradle.kts gradlew /usr/src/app/
COPY db/ /usr/src/app/db/
COPY gradle/ /usr/src/app/gradle/
## ワーキングディレクトリを指定
WORKDIR /usr/src/app
## bootjarビルド
RUN ./gradlew :backend:bootJar --no-daemon
## 生成されたファイル名が変更されても対応できるようにjarのファイル名をapp.jarに変更
RUN cp backend/build/libs/*.jar app.jar

## amazon corretto 21(ランタイム)
FROM amazoncorretto:21 AS runtime
## ビルドしたjarをコピー
COPY --from=builder /usr/src/app/app.jar .
## ポートを指定
EXPOSE 8080
## 起動コマンド
CMD ["java", "-jar", "app.jar"]
