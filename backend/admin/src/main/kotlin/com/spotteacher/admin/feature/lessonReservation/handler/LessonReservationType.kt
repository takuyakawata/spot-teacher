package com.spotteacher.admin.feature.lessonReservation.handler

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.admin.feature.lessonPlan.domain.LessonType
import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import java.time.LocalDate
import java.time.LocalTime


private const val LESSON_RESERVATION_TYPE = "LessonReservation"
private const val LESSON_RESERVATION_DATE_TYPE = "LessonReservationDate"

@GraphQLName(LESSON_RESERVATION_TYPE)
data class LessonReservationType(
    val id: ID,
    @GraphQLIgnore
    val lessonPlanId: LessonPlanId,
    @GraphQLIgnore
    val companyId: CompanyId,
    @GraphQLIgnore
    val reservedSchoolId: SchoolId,
    @GraphQLIgnore
    val teacherId: TeacherId,
    val lessonType: LessonType,
    val location: String,
    val reservationDates: List<LessonReservationDate>,
    val title: String,
    val description: String,
    val educations: List<ID>,
    val subjects: List<ID>,
    val grades: List<ID>,
){
    suspend fun lessonPlan(){}
    suspend fun company(){}
    suspend fun school(){}
    suspend fun teacher(){}
}

@GraphQLName(LESSON_RESERVATION_DATE_TYPE)
data class LessonReservationDate(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)