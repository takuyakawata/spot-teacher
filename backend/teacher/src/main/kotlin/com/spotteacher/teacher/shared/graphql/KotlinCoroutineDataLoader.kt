package com.spotteacher.teacher.shared.graphql

import arrow.core.Nel
import arrow.core.toNonEmptyListOrNull
import com.expediagroup.graphql.dataloader.KotlinDataLoader
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.dataloader.DataLoaderOptions
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Data loader interface that supports coroutine based data loading.
 * always use this suspend function to load data
 */
interface KotlinCoroutineDataLoader<K, V> : KotlinDataLoader<K, V> {
    override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<K, V> = DataLoaderFactory.newDataLoader(
        { keys, dataFetchEnvironment ->
            val coroutineScope = dataFetchEnvironment.getContext<GraphQLContext>()?.get<CoroutineScope>(keys)
                ?: CoroutineScope(EmptyCoroutineContext)
            coroutineScope.future {
                batchLoad(keys.toNonEmptyListOrNull()!!)
            }
        },
        DataLoaderOptions.newOptions()
            .setBatchLoaderContextProvider { graphQLContext }
    )

    suspend fun batchLoad(keys: Nel<K>): List<V>
}
