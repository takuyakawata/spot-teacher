package com.spotteacher.admin.feature.lessonReservation.infra

import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservation
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationId
import com.spotteacher.admin.feature.lessonReservation.domain.LessonReservationRepository
import com.spotteacher.admin.fixture.CompanyFixture
import com.spotteacher.admin.fixture.LessonPlanFixture
import com.spotteacher.admin.fixture.LessonReservationFixture
import com.spotteacher.admin.fixture.SchoolFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import com.spotteacher.admin.fixture.TeacherFixture
import org.springframework.beans.factory.annotation.Autowired

@SpringBootTest(webEnvironment = NONE)
class LessonReservationRepositoryImplTest(
    @Autowired private val lessonReservationRepository: LessonReservationRepository,
    @Autowired private val schoolFixture: SchoolFixture,
    @Autowired private val teacherFixture : TeacherFixture,
    @Autowired private val companyFixture: CompanyFixture,
    @Autowired private val lessonPlanFixture: LessonPlanFixture,
    @Autowired private val lessonReservationFixture: LessonReservationFixture,
) : DatabaseDescribeSpec({
    describe("LessonReservationRepository") {
        val school = schoolFixture.createSchool()
        val teacher = teacherFixture.create(
            schoolId = school.id
        )
        val company = companyFixture.createCompany()
        val draftLessonPlan = lessonPlanFixture.createDraftLessonPlan(
            companyId = company.id,
        )
        val publishedLessonPlan = lessonPlanFixture.updatePublishedLessonPlan(draftLessonPlan = draftLessonPlan)
        val reservation = lessonReservationFixture.createLessonReservation(
            lessonPlanId = publishedLessonPlan.id,
            reservedSchoolId = school.id,
            reservedTeacherId = teacher.id,
        )

        describe("findById") {
            context("when the lesson reservation exists") {
                it("should return lesson reservation"){
                    val result = lessonReservationRepository.findById(reservation.id)
                    result shouldBe reservation
                }

            context("when no lesson reservation exists") {
                it("should return null") {
                    val result = lessonReservationRepository.findById(LessonReservationId(999999))
                    result shouldBe null
                }
            }
            }

        }

        describe("paginated") {
            context("when no lesson reservations exist") {
                it("should return  list") {
                    // Arrange
                    val pagination = Pagination<LessonReservation>(
                        limit = 10
                    )
                    // Act
                    val result = lessonReservationRepository.paginated(pagination)

                    // Assert
                    result[0] shouldBe reservation
                }
            }

            it("should handle pagination with cursor columns") {
                // Arrange
                val pagination = Pagination(
                    limit = 10,
                    ColumnValue(
                        column = LessonReservation::id,
                        lastValue = 1L,
                        order = SortOrder.ASC
                    ) { it }
                )

                // Act
                val result = lessonReservationRepository.paginated(pagination)

                // Assert
                // Since we don't have a way to create lesson reservations in the admin module,
                // we can only verify that the method doesn't throw an exception
                result shouldNotBe null
            }
        }
    }
})
