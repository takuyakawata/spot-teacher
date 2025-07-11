package com.spotteacher.admin.feature.lessonTag.handler

import arrow.core.Nel
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.shared.graphql.KotlinCoroutineDataLoader
import graphql.schema.DataFetchingEnvironment
import org.dataloader.DataLoader
import org.springframework.stereotype.Component

private const val DATALOADER_NAME = "EducationDataLoader"

@Component
class EducationDataLoader(private val repository: EducationRepository) : KotlinCoroutineDataLoader<EducationId, EducationType> {
    override val dataLoaderName = DATALOADER_NAME

    override suspend fun batchLoad(keys: Nel<EducationId>): List<EducationType> {
        val educations = repository.filterByIds(keys)

        return educations.map { education ->
            EducationType(
                id = education.id.toGraphQLID(),
                name = education.name.value,
                isActive = education.isActive,
                displayOrder = education.displayOrder,
            )
        }
    }
}

fun DataFetchingEnvironment.getEducationDataLoader(): DataLoader<EducationId, EducationType?> {
    return this.getDataLoader<EducationId, EducationType?>(DATALOADER_NAME)!!
}
