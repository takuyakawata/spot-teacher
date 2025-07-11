import type { CodegenConfig } from '@graphql-codegen/cli';

const SCHEMA_URL = "../backend/admin/graphql/schema.graphql"

const config: CodegenConfig = {
  overwrite: true,
  schema: SCHEMA_URL,
  documents: ["src/**/*.tsx", "src/**/*.graphql"],
  generates: {
    "src/gql/graphql.ts": {
      plugins: [
        "typescript",
        "typescript-operations",
        "typescript-react-apollo"
      ]
    }
  },
  ignoreNoDocuments: true
};

export default config;