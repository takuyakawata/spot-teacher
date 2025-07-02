package com.spotteacher.admin.feature.lessonTag.usecase

import arrow.core.Either
import com.spotteacher.admin.feature.lessonTag.domain.Education
import com.spotteacher.admin.feature.lessonTag.domain.EducationError
import com.spotteacher.admin.feature.lessonTag.domain.EducationErrorCode
import com.spotteacher.admin.feature.lessonTag.domain.EducationId
import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.fixture.EducationFixture
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk

class CreateEducationUseCaseTest : DescribeSpec({
    val educationRepository = mockk<EducationRepository>()
    val useCase = CreateEducationUseCase(educationRepository)
    val educationFixture = EducationFixture()

    // テストデータの定義
    val validName = EducationName("New Programming Education")
    val validIsActive = true
    val validDisplayOrder = 10
    val existingName = EducationName("Existing Education")

    describe("CreateEducationUseCase") {
        describe("execute") {
            context("when a new education is successfully created") {
                it("should return success with the created education") {
                    // Arrange: このテストケース固有のモックの振る舞い設定
                    val createdEducation = educationFixture.buildEducation(
                        name = validName,
                        isActive = validIsActive,
                        displayOrder = validDisplayOrder,
                        id = EducationId(100L)
                    )
                    // repository.findByNameがnullを返し、名前が重複しないことを示す
                    coEvery { educationRepository.findByName(validName) } returns null
                    coEvery { educationRepository.create(any<Education>()) } returns createdEducation

                    // Act: UseCaseの実行
                    val result = useCase.call(name = validName)

                    // Assert: 結果の検証
                    result.shouldBeInstanceOf<Either.Right<Unit>>()
                    result.value shouldBe Unit
                }
            }
        }
        context("when education name is not empty") {
            it("should return education already exists error when education name is already used") {
                // Arrange: 既存のEducationが存在する場合をモック
                val existingEducation = educationFixture.buildEducation(name = existingName)
                coEvery { educationRepository.findByName(existingName) } returns existingEducation

                // Act: ユースケースの実行
                val result = useCase.call(name = existingName)

                // Assert: 結果の検証
                result.shouldBeInstanceOf<Either.Left<EducationError>>()
                result.value.code shouldBe EducationErrorCode.EDUCATION_ALREADY_EXISTS
                result.value.message shouldBe "Education already exists"
            }
        }
    }
})
