package com.spotteacher.teacher.feature.teacher.usecase

import com.spotteacher.teacher.feature.teacher.domain.ActiveTeacher
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import com.spotteacher.teacher.feature.teacher.domain.TeacherRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class CreateTeacherUseCaseTest : FunSpec({

    val teacherRepository = mockk<TeacherRepository>()
    val createTeacherUseCase = CreateTeacherUseCase(teacherRepository)

    test("should create teacher successfully") {
        // Given
        val userId = 1L
        val schoolId = 2L
        val input = CreateTeacherUseCaseInput(userId, schoolId)
        val expectedTeacher = ActiveTeacher(
            id = TeacherId(100),
            userId = userId,
            schoolId = schoolId
        )

        coEvery { teacherRepository.findByUserId(userId) } returns null
        coEvery { teacherRepository.create(any()) } returns expectedTeacher

        // When
        val result = createTeacherUseCase.call(input)

        // Then
        result shouldBe expectedTeacher
        coVerify { teacherRepository.findByUserId(userId) }
        coVerify { teacherRepository.create(any()) }
    }

    test("should throw exception when teacher already exists") {
        // Given
        val userId = 1L
        val schoolId = 2L
        val input = CreateTeacherUseCaseInput(userId, schoolId)
        val existingTeacher = ActiveTeacher(
            id = TeacherId(100),
            userId = userId,
            schoolId = schoolId
        )

        coEvery { teacherRepository.findByUserId(userId) } returns existingTeacher

        // When & Then
        shouldThrow<IllegalArgumentException> {
            createTeacherUseCase.call(input)
        }

        coVerify { teacherRepository.findByUserId(userId) }
        coVerify(exactly = 0) { teacherRepository.create(any()) }
    }
})
