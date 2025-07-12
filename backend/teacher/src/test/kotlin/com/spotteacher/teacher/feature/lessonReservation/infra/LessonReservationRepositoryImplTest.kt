package com.spotteacher.teacher.feature.lessonReservation.infra

import arrow.core.nonEmptyListOf
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDate
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDates
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationDescription
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationEducations
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationGrades
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationSubjects
import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationTitle
import com.spotteacher.teacher.feature.lessonReservation.domain.ReservationLessonLocation
import com.spotteacher.teacher.feature.lessonReservation.domain.TeacherId
import com.spotteacher.teacher.feature.school.domain.SchoolId
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest(webEnvironment = NONE)
class LessonReservationRepositoryImplTest(
    @Autowired private val lessonReservationRepository: LessonReservationRepository
) : DatabaseDescribeSpec({

    describe("LessonReservationRepository") {
        describe("create") {
            it("should create a lesson reservation and return it with an ID") {
                // Arrange - Create test data
                val lessonReservation = LessonReservation(
                    id = LessonReservationId(0), // Will be replaced by the repository
                    lessonPlanId = LessonPlanId(1),
                    reservedSchoolId = SchoolId(1),
                    reservedTeacherId = TeacherId(1),
                    educations = LessonReservationEducations(emptySet()),
                    subjects = LessonReservationSubjects(emptySet()),
                    grades = LessonReservationGrades(emptySet()),
                    title = LessonReservationTitle("Test Lesson Reservation"),
                    description = LessonReservationDescription("Test Description"),
                    lessonType = LessonType.OFFLINE,
                    location = ReservationLessonLocation("Test Location"),
                    reservationDates = LessonReservationDates(nonEmptyListOf(
                        LessonReservationDate(
                            date = LocalDate.now().plusDays(7),
                            startTime = LocalTime.of(9, 0),
                            endTime = LocalTime.of(17, 0)
                        )
                    ))
                )

                // Act
                val createdLessonReservation = lessonReservationRepository.create(lessonReservation)

                // Assert
                createdLessonReservation.id.value shouldNotBe 0
                createdLessonReservation.title shouldBe lessonReservation.title
                createdLessonReservation.description shouldBe lessonReservation.description
                createdLessonReservation.lessonType shouldBe lessonReservation.lessonType
                createdLessonReservation.location shouldBe lessonReservation.location
                createdLessonReservation.reservationDates.value.size shouldBe lessonReservation.reservationDates.value.size
            }
        }
    }
})