package com.spotteacher.teacher.feature.lessonSchedule.infra

import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.teacher.feature.lessonSchedule.domain.LessonScheduleId
import com.spotteacher.teacher.feature.lessonSchedule.domain.LessonScheduleRepository
import com.spotteacher.teacher.fixture.CompanyFixture
import com.spotteacher.teacher.fixture.LessonPlanFixture
import com.spotteacher.teacher.fixture.LessonReservationFixture
import com.spotteacher.teacher.fixture.LessonScheduleFixture
import com.spotteacher.teacher.fixture.SchoolFixture
import com.spotteacher.teacher.fixture.TeacherFixture
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class LessonScheduleRepositoryImplTest(
    @Autowired private val lessonScheduleRepository: LessonScheduleRepository,
    @Autowired private val lessonScheduleFixture: LessonScheduleFixture,
    @Autowired private val lessonReservationFixture: LessonReservationFixture,
    @Autowired private val schoolFixture: SchoolFixture,
    @Autowired private val teacherFixture: TeacherFixture,
    @Autowired private val companyFixture: CompanyFixture,
    @Autowired private val lessonPlanFixture: LessonPlanFixture
) : DatabaseDescribeSpec({

    describe("LessonScheduleRepository") {
        val school = schoolFixture.createSchool()
        val teacher = teacherFixture.create(schoolId = school.id)
        
        describe("create") {
            it("should create a lesson schedule and return it with an ID") {
                // Arrange - Create test data
                val company = companyFixture.createCompany()
                val lessonPlan = lessonPlanFixture.create(companyId = company.id)
                val lessonReservation = lessonReservationFixture.create(
                    lessonPlanId = lessonPlan.id,
                    reservedSchoolId = school.id,
                    reservedTeacherId = teacher.id
                )
                
                val lessonSchedule = lessonScheduleFixture.buildDoingLessonSchedule(
                    id = LessonScheduleId(0),
                    lessonReservationId = lessonReservation.id,
                    reservedSchoolId = school.id,
                    reservedTeacherId = teacher.id
                )

                // Act
                val createdLessonSchedule = lessonScheduleRepository.create(lessonSchedule)

                // Assert
                createdLessonSchedule.id.value shouldNotBe 0
                createdLessonSchedule.title shouldBe lessonSchedule.title
                createdLessonSchedule.description shouldBe lessonSchedule.description
                createdLessonSchedule.lessonType shouldBe lessonSchedule.lessonType
                createdLessonSchedule.location shouldBe lessonSchedule.location
                createdLessonSchedule.scheduleDate.date shouldBe lessonSchedule.scheduleDate.date
                createdLessonSchedule.scheduleDate.startTime shouldBe lessonSchedule.scheduleDate.startTime
                createdLessonSchedule.scheduleDate.endTime shouldBe lessonSchedule.scheduleDate.endTime
            }
        }

        describe("findById") {
            it("should find a lesson schedule by ID") {
                // Arrange - Create test data
                val company = companyFixture.createCompany()
                val lessonPlan = lessonPlanFixture.create(companyId = company.id)
                val lessonReservation = lessonReservationFixture.create(
                    lessonPlanId = lessonPlan.id,
                    reservedSchoolId = school.id,
                    reservedTeacherId = teacher.id
                )
                
                val lessonSchedule = lessonScheduleFixture.buildDoingLessonSchedule(
                    id = LessonScheduleId(0),
                    lessonReservationId = lessonReservation.id,
                    reservedSchoolId = school.id,
                    reservedTeacherId = teacher.id
                )

                // Create the lesson schedule
                val createdLessonSchedule = lessonScheduleRepository.create(lessonSchedule)

                // Act - Find the lesson schedule by ID
                val foundLessonSchedule = lessonScheduleRepository.findById(createdLessonSchedule.id)

                // Assert
                foundLessonSchedule shouldNotBe null
                foundLessonSchedule?.id shouldBe createdLessonSchedule.id
                foundLessonSchedule?.title shouldBe createdLessonSchedule.title
                foundLessonSchedule?.description shouldBe createdLessonSchedule.description
                foundLessonSchedule?.lessonType shouldBe createdLessonSchedule.lessonType
                foundLessonSchedule?.location shouldBe createdLessonSchedule.location
            }

            it("should return null when finding a non-existent lesson schedule") {
                // Act - Try to find a non-existent lesson schedule
                val foundLessonSchedule = lessonScheduleRepository.findById(LessonScheduleId(999999))

                // Assert
                foundLessonSchedule shouldBe null
            }
        }
    }
})