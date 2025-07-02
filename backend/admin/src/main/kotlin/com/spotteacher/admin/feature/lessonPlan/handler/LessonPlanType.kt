package com.spotteacher.admin.feature.lessonPlan.handler

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.Grade
import com.spotteacher.admin.feature.lessonTag.domain.Subject
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.graphql.toID
import java.time.LocalDateTime
import java.time.LocalTime

private const val LESSON_PLAN_TYPE = "LessonPlan"
private const val DRAFT_LESSON_PLAN_TYPE = "DraftLessonPlan"
private const val PUBLISHED_LESSON_PLAN_TYPE = "PublishedLessonPlan"
private const val LESSON_PLAN_DATE_TYPE = "LessonPlanDate"

@GraphQLName(LESSON_PLAN_TYPE)
sealed interface LessonPlanType {
    val id: ID

    @GraphQLIgnore
    val companyId: CompanyId
    val createdAt: LocalDateTime

    @GraphQLIgnore
    val images: List<UploadFileId>
}

fun LessonPlanId.toGraphQLID() = this.toID(LESSON_PLAN_TYPE)

@GraphQLName(PUBLISHED_LESSON_PLAN_TYPE)
data class PublishedLessonPlanType(
    override val id: ID,
    @GraphQLIgnore
    override val companyId: CompanyId,
    @GraphQLIgnore
    override val images: List<UploadFileId>,
    override val createdAt: LocalDateTime,
    val title: String,
    val description: String,
    val lessonType: LessonType,
    val location: String,
    val annualMaxExecutions: Int,
    val lessonPlanDates: List<LessonPlanDateType>,
    @GraphQLIgnore
    val lessonPlanEducations: List<EducationId>,
    val lessonPlanSubjects: List<Subject>,
    val lessonPlanGrades: List<Grade>
) : LessonPlanType {
    // todo add date loader
    // educations
    // uploadFile
    // companyは必要なら
}

@GraphQLName(DRAFT_LESSON_PLAN_TYPE)
data class DraftLessonPlanType(
    override val id: ID,
    @GraphQLIgnore
    override val companyId: CompanyId,
    @GraphQLIgnore
    override val images: List<UploadFileId>,
    override val createdAt: LocalDateTime,
    val title: String?,
    val description: String?,
    val lessonType: LessonType?,
    val location: String?,
    val annualMaxExecutions: Int?,
    val lessonPlanDates: List<LessonPlanDateType>?,
    @GraphQLIgnore
    val lessonPlanEducations: List<EducationId>,
    val lessonPlanSubjects: List<Subject>,
    val lessonPlanGrades: List<Grade>
) : LessonPlanType {
    // todo add date loader
    // educations
    // uploadFiles
    // companyは必要なら
}

@GraphQLName(LESSON_PLAN_DATE_TYPE)
data class LessonPlanDateType(
    val startMonth: Int,
    val startDay: Int,
    val endMonth: Int,
    val endDay: Int,
    val startTime: LocalTime,
    val endTime: LocalTime
)

data class LessonPlanDateInput(
    val startMonth: Int,
    val startDay: Int,
    val endMonth: Int,
    val endDay: Int,
    val startTime: LocalTime,
    val endTime: LocalTime
)
