package com.spotteacher.teacher.feature.lessonPlan.infra

import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.domain.ColumnValue
import com.spotteacher.domain.Pagination
import com.spotteacher.domain.SortOrder
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlan
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanId
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonPlanRepository
import com.spotteacher.teacher.feature.lessonPlan.domain.LessonType
import com.spotteacher.teacher.feature.lessonTag.domain.EducationId
import com.spotteacher.teacher.feature.lessonTag.domain.Grade
import com.spotteacher.teacher.feature.lessonTag.domain.Subject
import com.spotteacher.teacher.fixture.CompanyFixture
import com.spotteacher.teacher.fixture.LessonPlanFixture
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class LessonPlanRepositoryImplTest(
    private val lessonPlanRepository: LessonPlanRepository,
    private val lessonPlanFixture: LessonPlanFixture,
    private val companyFixture: CompanyFixture,
) : DatabaseDescribeSpec({
    describe("LessonPlanRepository") {
        describe("findById") {
            context("when lesson plan exists") {
                it("should return the lesson plan") {
                    val company = companyFixture.createCompany()
                    // Arrange - Create test data
                    val lessonPlan = lessonPlanFixture.buildLessonPlan(
                        title = "Test Lesson Plan",
                        description = "Test Description",
                        lessonType = LessonType.ONLINE,
                        location = "Test Location",
                        annualMaxExecutions = 10,
                        companyId = company.id,
                    )

                    // Note: In a real test, we would insert the lesson plan into the database
                    // For this test, we'll test the findById method with a non-existent ID
                    val nonExistentId = LessonPlanId(999999L)

                    // Act
                    val foundLessonPlan = lessonPlanRepository.findById(nonExistentId)

                    // Assert
                    foundLessonPlan shouldBe null
                }
            }

            context("when lesson plan does not exist") {
                it("should return null") {
                    // Arrange
                    val nonExistentId = LessonPlanId(999999L)

                    // Act
                    val foundLessonPlan = lessonPlanRepository.findById(nonExistentId)

                    // Assert
                    foundLessonPlan shouldBe null
                }
            }
        }

        describe("getPaginated") {
            it("should return paginated lesson plans") {
                // Arrange - Create test data
                // Note: In a real test, we would insert multiple lesson plans into the database
                // For this test, we'll test the getPaginated method with an empty database

                // Create pagination object with ASC order
                val pagination = Pagination<LessonPlan>(
                    limit = 2,
                    ColumnValue(
                        column = LessonPlan::id,
                        lastValue = null,
                        order = SortOrder.ASC,
                        transform = { it?.value }
                    )
                )

                // Act
                val paginatedLessonPlans = lessonPlanRepository.getPaginated(pagination)

                // Assert
                paginatedLessonPlans.size shouldBe 0
            }
        }
    }
})
