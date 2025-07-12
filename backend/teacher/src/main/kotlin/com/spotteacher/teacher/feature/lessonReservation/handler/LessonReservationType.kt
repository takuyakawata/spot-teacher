package com.spotteacher.teacher.feature.lessonReservation.handler

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.generator.scalars.ID
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import java.time.LocalDate
import java.time.LocalTime

private const val LESSON_RESERVATION_TYPE = "LessonReservation"
private const val LESSON_RESERVATION_DATE_TYPE = "LessonReservationDate"

@GraphQLName(LESSON_RESERVATION_TYPE)
data class LessonReservationType(
    val id: ID,
    val lessonId: ID,
    val companyId: ID,
    val reservedSchoolId: ID,
    val userId: ID,
    val lessonType: LessonType,
    val location: String,
    val reservationDates: List<LessonReservationDate>,
    val title: String,
    val description: String,
    val educations: List<ID>,
    val subjects: List<ID>,
    val grades: List<ID>,
)

@GraphQLName(LESSON_RESERVATION_DATE_TYPE)
data class LessonReservationDate(
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)