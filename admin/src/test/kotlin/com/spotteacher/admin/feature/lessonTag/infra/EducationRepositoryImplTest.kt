package com.spotteacher.admin.feature.lessonTag.infra

import arrow.core.nonEmptyListOf
import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.fixture.EducationFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE

@SpringBootTest(webEnvironment = NONE)
class EducationRepositoryImplTest(
    private val educationRepository: EducationRepository,
    private val educationFixture: EducationFixture
) : DatabaseDescribeSpec({

    describe("EducationRepository") {
        describe("create and findByName") {
            it("should create an Education and find it by name") {
                // Create a new Education
                val education = educationFixture.buildEducation(
                    name = EducationName("Test Education"),
                    isActive = true,
                    displayOrder = 1
                )

                // Create the education in the repository
                val createdEducation = educationRepository.create(education)

                // Find the education by name
                val foundEducation = educationRepository.findByName(EducationName("Test Education"))

                // Verify the education was found and has the correct properties
                foundEducation shouldNotBe null
                foundEducation!!.name.value shouldBe "Test Education"
                foundEducation.isActive shouldBe true
                foundEducation.displayOrder shouldBe 1
            }
        }

        describe("update") {
            it("should update an Education") {
                // Create a new Education
                val education = educationFixture.createEducation(
                    name = EducationName("Education to Update"),
                    isActive = true,
                    displayOrder = 2
                )

                // Update the education
                val updatedEducation = education.copy(
                    name = EducationName("Updated Education"),
                    isActive = false,
                    displayOrder = 3
                )
                educationRepository.update(updatedEducation)

                // Find the education by name
                val foundEducation = educationRepository.findByName(EducationName("Updated Education"))

                // Verify the education was updated
                foundEducation shouldNotBe null
                foundEducation!!.name.value shouldBe "Updated Education"
                foundEducation.isActive shouldBe false
                foundEducation.displayOrder shouldBe 3
            }
        }

        describe("delete") {
            it("should delete an Education") {
                // Create a new Education
                val education = educationFixture.createEducation(
                    name = EducationName("Education to Delete"),
                    isActive = true,
                    displayOrder = 4
                )

                // Find the education by name to verify it exists
                val foundEducation = educationRepository.findByName(EducationName("Education to Delete"))
                foundEducation shouldNotBe null

                // Delete the education
                educationRepository.delete(education.id)

                // Verify the education no longer exists
                val deletedEducation = educationRepository.findByName(EducationName("Education to Delete"))
                deletedEducation shouldBe null
            }
        }

        describe("getAll") {
            it("should return all educations") {
                // Create multiple educations
                val education1 = educationFixture.createEducation(
                    name = EducationName("Education One"),
                    isActive = true,
                    displayOrder = 5
                )
                val education2 = educationFixture.createEducation(
                    name = EducationName("Education Two"),
                    isActive = false,
                    displayOrder = 6
                )

                // Get all educations
                val allEducations = educationRepository.getAll()

                // Verify the educations are returned
                allEducations.shouldNotBeEmpty()

                // Find the created educations in the list
                val foundEducation1 = allEducations.find { it.name.value == "Education One" }
                val foundEducation2 = allEducations.find { it.name.value == "Education Two" }

                foundEducation1 shouldNotBe null
                foundEducation2 shouldNotBe null

                foundEducation1!!.isActive shouldBe true
                foundEducation1.displayOrder shouldBe 5

                foundEducation2!!.isActive shouldBe false
                foundEducation2.displayOrder shouldBe 6
            }
        }

        describe("filterByIds") {
            it("should filter educations by IDs") {
                // Create multiple educations
                val education1 = educationFixture.createEducation(
                    name = EducationName("Filter Education One"),
                    isActive = true,
                    displayOrder = 7
                )
                val education2 = educationFixture.createEducation(
                    name = EducationName("Filter Education Two"),
                    isActive = true,
                    displayOrder = 8
                )
                val education3 = educationFixture.createEducation(
                    name = EducationName("Filter Education Three"),
                    isActive = true,
                    displayOrder = 9
                )

                // Filter educations by IDs
                val filteredEducations = educationRepository.filterByIds(
                    nonEmptyListOf(education1.id, education3.id)
                )

                // Verify the filtered educations
                filteredEducations.size shouldBe 2
                filteredEducations.map { it.id } shouldContain education1.id
                filteredEducations.map { it.id } shouldContain education3.id
            }
        }
    }
})
