package com.spotteacher.admin.fixture

import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EducationFixture {

    @Autowired
    private lateinit var repository: EducationRepository

    private var companyIdCount = 1L

    fun buildEducation(
        id: EducationId = EducationId(companyIdCount++),
        name: EducationName = EducationName("test education"),
        isActive: Boolean = true,
        displayOrder: Int = 0,
    ): Education {
        return Education(
            id = id,
            name = name,
            isActive = isActive,
            displayOrder = displayOrder
        )
    }

    suspend fun createEducation(
        name: EducationName = EducationName("test education"),
        isActive: Boolean = true,
        displayOrder: Int = 0,
    ): Education {
        return repository.create(
            buildEducation(
                name = name,
                isActive = isActive,
                displayOrder = displayOrder,
            )
        )
    }
}
