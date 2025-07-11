package com.spotteacher.teacher.feature.lessonPlan.handler

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.graphql.toID
import com.spotteacher.teacher.feature.company.domain.CompanyId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.feature.uploadFile.domain.UploadFileId
import java.time.LocalDateTime
import java.time.LocalTime

private const val LESSON_PLAN_TYPE = "LessonPlan"
private const val DRAFT_LESSON_PLAN_TYPE = "DraftLessonPlan"
private const val PUBLISHED_LESSON_PLAN_TYPE = "PublishedLessonPlan"
private const val LESSON_PLAN_DATE_TYPE = "LessonPlanDate"

fun LessonPlanId.toGraphQLID() = this.toID(LESSON_PLAN_TYPE)

@GraphQLName(PUBLISHED_LESSON_PLAN_TYPE)
data class LessonPlanType(
    val id: ID,
    @GraphQLIgnore
    val companyId: CompanyId,
    @GraphQLIgnore
    val images: List<UploadFileId>,
    val createdAt: LocalDateTime,
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
) {
    // todo add date loader
    // educations
    // uploadFile
    // company
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
