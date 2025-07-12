package com.spotteacher.teacher.feature.lessonReservation.handler.mutation

import arrow.core.nonEmptyListOf
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.spotteacher.graphql.NonEmptyString
import com.spotteacher.graphql.toDomainId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDate
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDates
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.teacher.feature.lessonReservation.handler.LessonReservationType
import com.spotteacher.teacher.feature.lessonReservation.usecase.CreateLessonReservationUseCase
import com.spotteacher.teacher.feature.lessonReservation.usecase.CreateLessonReservationUseCaseInput
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalTime

data class CreateLessonReservationMutationInput(
    val teacherId: ID,//todo authでとれるようにする。
    val schoolId: ID,//todo authでとれるようにする。
    val lessonPlanId: ID,
    val companyId: ID,//todo lesson planから取得。
    val title: NonEmptyString,
    val description: NonEmptyString,
    val startTime: NonEmptyString,
    val endTime:NonEmptyString,
    val location: NonEmptyString,
    val educations: List<ID>,
    val subjects: List<Subject>,
    val grades: List<Grade>,
    val lessonType: LessonReservationType
)

@Component
class CreateLessonReservationMutation(
    private val useCase: CreateLessonReservationUseCase
) : Mutation {
    suspend fun createLessonReservation(input: CreateLessonReservationMutationInput) {
        useCase.call(
            CreateLessonReservationUseCaseInput(
                teacherId = input.teacherId.toDomainId(::TeacherId),
                schoolId = input.schoolId.toDomainId(::SchoolId),
                lessonPlanId = input.lessonPlanId.toDomainId(::LessonPlanId),
                title = LessonReservationTitle(input.title.value),
                description = LessonReservationTitle(input.description.value),
                location = ReservationLessonLocation(input.location.value),
                lessonType = input.lessonType,
                educations = LessonReservationEducations(
                    input.educations.map { it.toDomainId(::EducationId) }.toSet()
                ),
                subjects = LessonReservationSubjects(
                    input.subjects.toSet()
                ),
                grades = LessonReservationGrades(
                    input.grades.toSet()
                ),
                lessonReservationDates = LessonReservationDates(
                    nonEmptyListOf(
                        LessonReservationDate(
                            date = LocalDate.now(),
                            startTime = LocalTime.parse(input.startTime.value),
                            endTime = LocalTime.parse(input.endTime.value)
                        )
                    )
                )
            )
        )

    }
}
