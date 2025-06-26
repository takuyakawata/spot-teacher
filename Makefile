help: ## makefileに記載されているコマンドを一覧表示
	@echo "Usage: make [command]"
	@echo ""
	@echo "Commands:"
	@egrep "^(.+)\:\ ##\ (.+)" ${MAKEFILE_LIST} | column -t -c 2 -s ':#'

# 主なコマンドたち
## localでbuild
build-local: ## localでbuild
	docker compose build --no-cache

##localでrun
run: ## localでrun
	docker compose up

## databaseのmigration
db-migrate: db-migrate-exec db-migrate-info jooq-codegen

mysql-reset:
	docker-compose down && rm -rf ./local-db/mysql

db-repair:
	./gradlew flywayRepair --no-configuration-cache
	@make db-migrate-info

# コマンドの依存関係
db-migrate-exec:
	./gradlew flywayMigrate --no-configuration-cache

db-migrate-info:
	./gradlew flywayInfo --no-configuration-cache

.PHONY: jooq-codegen
jooq-codegen:
	./gradlew db:jooqCodegen --no-configuration-cache

admin-graphql-codegen:
	./gradlew admin:graphqlGenerateSDL --no-configuration-cache
detekt:
	./gradlew detekt --auto-correct
