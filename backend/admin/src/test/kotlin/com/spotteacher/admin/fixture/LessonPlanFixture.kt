package com.spotteacher.admin.fixture

import arrow.core.Nel
import arrow.core.toNonEmptyListOrNull
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.DraftLessonPlan
import com.spotteacher.admin.feature.lessonPlan.domain.LessonLocation
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDate
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanDescription
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanEducations
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanGrades
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanSubjects
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanTitle
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonPlan.domain.PublishedLessonPlan
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class LessonPlanFixture {

    @Autowired
    private lateinit var lessonPlanRepository: LessonPlanRepository

    private var lessonPlanIdCount = 1L

    // Helper function to create a default LessonPlanDate
    fun defaultLessonPlanDate() = LessonPlanDate(
        startMonth = 1,
        startDay = 1,
        endMonth = 12,
        endDay = 31,
        startTime = LocalTime.of(9, 0),
        endTime = LocalTime.of(17, 0)
    )

    // Helper function to create a default Nel<LessonPlanDate>
    fun defaultLessonPlanDates(): Nel<LessonPlanDate> {
        return listOf(defaultLessonPlanDate()).toNonEmptyListOrNull()!!
    }

    fun defaultEducations() = LessonPlanEducations(emptySet())
    fun defaultSubjects() = LessonPlanSubjects(emptySet())
    fun defaultGrades() = LessonPlanGrades(emptySet())

    // Build a DraftLessonPlan with default values
    fun buildDraftLessonPlan(
        id: LessonPlanId = LessonPlanId(lessonPlanIdCount++),
        companyId: CompanyId,
        images: List<UploadFileId> = emptyList(),
        createdAt: LocalDateTime = LocalDateTime.now(),
        title: LessonPlanTitle? = LessonPlanTitle("Draft Lesson Plan"),
        description: LessonPlanDescription? = LessonPlanDescription("This is a draft lesson plan description"),
        lessonType: LessonType? = LessonType.ONLINE,
        location: LessonLocation? = LessonLocation("Test Location"),
        annualMaxExecutions: Int? = 10,
        lessonPlanDates: Nel<LessonPlanDate>? = defaultLessonPlanDates(),
        educations: LessonPlanEducations = defaultEducations(),
        subjects: LessonPlanSubjects = defaultSubjects(),
        grades: LessonPlanGrades = defaultGrades()
    ): DraftLessonPlan {
        return DraftLessonPlan(
            id = id,
            companyId = companyId,
            images = images,
            createdAt = createdAt,
            title = title,
            description = description,
            lessonType = lessonType,
            location = location,
            annualMaxExecutions = annualMaxExecutions,
            lessonPlanDates = lessonPlanDates,
            educations = educations,
            subjects = subjects,
            grades = grades
        )
    }

    // Build a PublishedLessonPlan with default values
    fun buildPublishedLessonPlan(
        id: LessonPlanId = LessonPlanId(lessonPlanIdCount++),
        companyId: CompanyId,
        images: List<UploadFileId> = emptyList(),
        createdAt: LocalDateTime = LocalDateTime.now(),
        title: LessonPlanTitle = LessonPlanTitle("Published Lesson Plan"),
        description: LessonPlanDescription = LessonPlanDescription("This is a published lesson plan description"),
        lessonType: LessonType = LessonType.ONLINE,
        location: LessonLocation = LessonLocation("Test Location"),
        annualMaxExecutions: Int = 10,
        lessonPlanDates: Nel<LessonPlanDate> = defaultLessonPlanDates(),
        educations: LessonPlanEducations = defaultEducations(),
        subjects: LessonPlanSubjects = defaultSubjects(),
        grades: LessonPlanGrades = defaultGrades()
    ): PublishedLessonPlan {
        return PublishedLessonPlan(
            id = id,
            companyId = companyId,
            images = images,
            createdAt = createdAt,
            title = title,
            description = description,
            lessonType = lessonType,
            location = location,
            annualMaxExecutions = annualMaxExecutions,
            lessonPlanDates = lessonPlanDates,
            educations = educations,
            subjects = subjects,
            grades = grades,
        )
    }

    // Create and persist a DraftLessonPlan
    suspend fun createDraftLessonPlan(
        companyId: CompanyId,
        images: List<UploadFileId> = emptyList(),
        title: LessonPlanTitle? = LessonPlanTitle("Draft Lesson Plan"),
        description: LessonPlanDescription? = LessonPlanDescription("This is a draft lesson plan description"),
        lessonType: LessonType? = LessonType.ONLINE,
        location: LessonLocation? = LessonLocation("Test Location"),
        annualMaxExecutions: Int? = 10,
        lessonPlanDates: Nel<LessonPlanDate>? = defaultLessonPlanDates(),
        educations: LessonPlanEducations = defaultEducations(),
        subjects: LessonPlanSubjects = defaultSubjects(),
        grades: LessonPlanGrades = defaultGrades()
    ): DraftLessonPlan {
        val draftLessonPlan = DraftLessonPlan.create(
            companyId = companyId,
            title = title,
            description = description,
            lessonType = lessonType,
            location = location,
            annualMaxExecutions = annualMaxExecutions,
            lessonPlanDates = lessonPlanDates,
            educations = educations,
            subjects = subjects,
            grades = grades,
        )
        return lessonPlanRepository.createDraft(draftLessonPlan)
    }

    // Convert a DraftLessonPlan to PublishedLessonPlan and update it
    suspend fun publishDraftLessonPlan(draftLessonPlan: DraftLessonPlan): PublishedLessonPlan {
        val publishedLessonPlan = draftLessonPlan.toPublishedLessonPlan()
        lessonPlanRepository.updateStatus(publishedLessonPlan)
        return publishedLessonPlan
    }
}
