package com.spotteacher.admin.feature.lessonTag.usecase

import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.fixture.EducationFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class FindEducationsFilterByNameUseCaseTest : DescribeSpec({


    val educationRepository = mockk<EducationRepository>()
    val useCase = FindEducationsFilterByNameUseCase(educationRepository)
    val educationFixture = EducationFixture()

    describe("FindEducationsFilterByNameUseCase") {
        describe("call") {
            context("when an education with the given name exists") {
                it("should return the matching education") {
                    val educationName = EducationName("Existing Program")
                    val expectedEducation = educationFixture.buildEducation(name = educationName)

                    coEvery { educationRepository.findByName(educationName) } returns expectedEducation

                    val result = useCase.call(name = educationName)

                    result shouldBe expectedEducation
                }
            }

            context("when no education with the given name exists") {
                it("should return null") {
                    val nonExistentName = EducationName("Non-existent Program")

                    coEvery { educationRepository.findByName(nonExistentName) } returns null

                    val result = useCase.call(name = nonExistentName)

                    result shouldBe null
                }
            }
        }
    }

})
