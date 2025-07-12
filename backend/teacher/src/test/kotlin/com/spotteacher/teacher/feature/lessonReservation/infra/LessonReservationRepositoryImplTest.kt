package com.spotteacher.teacher.feature.lessonReservation.infra

import arrow.core.nonEmptyListOf
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
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
import com.spotteacher.teacher.fixture.LessonReservationFixture
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import java.time.LocalDate
import java.time.LocalTime

@SpringBootTest(webEnvironment = NONE)
class LessonReservationRepositoryImplTest(
    @Autowired private val lessonReservationRepository: LessonReservationRepository,
    @Autowired private val lessonReservationFixture: LessonReservationFixture,
    @Autowired private val schoolFixture: SchoolFixture,
) : DatabaseDescribeSpec({

    describe("LessonReservationRepository") {
        describe("create") {
            it("should create a lesson reservation and return it with an ID") {
                // Arrange - Create test data
                val lessonReservation = lessonReservationFixture.buildLessonReservation(
                    id = LessonReservationId(0) // Will be replaced by the repository
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

        describe("filterByTeacherAndSchoolId") {
            it("should filter lesson reservations by teacher ID and school ID with pagination") {
                // Arrange - Create test data
                val teacherId = TeacherId(1)
                val schoolId = SchoolId(1)
                val lessonReservation = lessonReservationFixture.buildLessonReservation(
                    id = LessonReservationId(0), // Will be replaced by the repository
                    reservedSchoolId = schoolId,
                    reservedTeacherId = teacherId,
                    title = "Test Lesson Reservation for Filtering",
                    description = "Test Description for Filtering",
                    location = "Test Location for Filtering"
                )

                // Create the lesson reservation
                val createdLessonReservation = lessonReservationRepository.create(lessonReservation)

                // Create pagination with no cursor (get all results up to limit)
                val pagination = Pagination<LessonReservation>(
                    limit = 10
                )

                // Act - Filter lesson reservations
                val filteredLessonReservations = lessonReservationRepository.filterByTeacherAndSchoolId(
                    teacherId = teacherId,
                    schoolId = schoolId,
                    pagination = pagination
                )

                // Assert
                filteredLessonReservations.shouldNotBe(null)
                filteredLessonReservations.isNotEmpty() shouldBe true
                val foundReservation = filteredLessonReservations.find { it.id == createdLessonReservation.id }
                foundReservation.shouldNotBe(null)
                foundReservation?.title shouldBe lessonReservation.title
                foundReservation?.description shouldBe lessonReservation.description
                foundReservation?.lessonType shouldBe lessonReservation.lessonType
                foundReservation?.location shouldBe lessonReservation.location
            }

            it("should filter lesson reservations with cursor-based pagination") {
                // Arrange - Create test data
                val teacherId = TeacherId(1)
                val schoolId = SchoolId(1)

                // Create multiple lesson reservations
                val lessonReservation1 = lessonReservationRepository.create(
                    lessonReservationFixture.buildLessonReservation(
                        id = LessonReservationId(0),
                        reservedSchoolId = schoolId,
                        reservedTeacherId = teacherId,
                        title = "Test Lesson Reservation 1",
                        description = "Test Description 1",
                        location = "Test Location 1"
                    )
                )

                val lessonReservation2 = lessonReservationRepository.create(
                    lessonReservationFixture.buildLessonReservation(
                        id = LessonReservationId(0),
                        reservedSchoolId = schoolId,
                        reservedTeacherId = teacherId,
                        title = "Test Lesson Reservation 2",
                        description = "Test Description 2",
                        location = "Test Location 2",
                        date = LocalDate.now().plusDays(8)
                    )
                )

                // Create pagination with cursor (get results after the first reservation)
                val pagination = Pagination(
                    limit = 10,
                    ColumnValue(
                        column = LessonReservation::id,
                        lastValue = lessonReservation1.id.value,
                        order = SortOrder.ASC
                    ) { it }
                )

                // Act - Filter lesson reservations with cursor
                val filteredLessonReservations = lessonReservationRepository.filterByTeacherAndSchoolId(
                    teacherId = teacherId,
                    schoolId = schoolId,
                    pagination = pagination
                )

                // Assert
                filteredLessonReservations.shouldNotBe(null)
                filteredLessonReservations.isNotEmpty() shouldBe true

                // The first reservation should not be included (it's before the cursor)
                filteredLessonReservations.find { it.id == lessonReservation1.id } shouldBe null

                // The second reservation should be included (it's after the cursor)
                val foundReservation = filteredLessonReservations.find { it.id == lessonReservation2.id }
                foundReservation.shouldNotBe(null)
                foundReservation?.title shouldBe lessonReservation2.title
            }
        }
    }
})
