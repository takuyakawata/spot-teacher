import type { CodegenConfig } from '@graphql-codegen/cli';

const SCHEMA_URL = "https://spacex-production.up.railway.app/"

const config: CodegenConfig = {
  overwrite: true,
  schema: SCHEMA_URL,
  documents: "src/**/*.tsx",
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