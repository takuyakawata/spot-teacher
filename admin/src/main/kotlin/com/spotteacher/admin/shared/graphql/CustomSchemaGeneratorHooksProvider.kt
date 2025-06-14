package com.spotteacher.admin.shared.graphql

import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.plugin.schema.hooks.SchemaGeneratorHooksProvider
import com.spotteacher.admin.shared.graphql.CustomSchemaGeneratorHooks

class CustomSchemaGeneratorHooksProvider : SchemaGeneratorHooksProvider {
    override fun hooks(): SchemaGeneratorHooks {
        return CustomSchemaGeneratorHooks()
    }
}
