package com.spotteacher.admin.feature.lessonTag.usecase

import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.fixture.EducationFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk

class GetAllEducationsUseCaseTest : DescribeSpec({
    val educationRepository = mockk<EducationRepository>()
    val useCase = GetAllEducationsUseCase(educationRepository)
    val educationFixture = EducationFixture()

    describe("GetAllEducationsUseCase") {
        describe("call") {
            context("when there are existing educations") {
                it("should return a list of all educations") {
                    val education1 = educationFixture.buildEducation(id = EducationId(1L), name = EducationName("Math Education"))
                    val education2 = educationFixture.buildEducation(id = EducationId(2L), name = EducationName("Science Program"))
                    val expectedEducations = listOf(education1, education2)

                    coEvery { educationRepository.getAll() } returns expectedEducations

                    val result = useCase.call()

                    result shouldBe expectedEducations
                }
            }

            context("when there are no existing educations") {
                it("should return an empty list") {
                    coEvery { educationRepository.getAll() } returns emptyList()

                    val result = useCase.call()

                    result shouldBe emptyList()
                }
            }
        }
    }
})
