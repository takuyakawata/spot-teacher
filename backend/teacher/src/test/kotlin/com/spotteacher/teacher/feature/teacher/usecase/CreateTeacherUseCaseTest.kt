package com.spotteacher.teacher.feature.teacher.usecase

import com.spotteacher.domain.EmailAddress
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.Teacher
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import com.spotteacher.teacher.feature.teacher.domain.TeacherName
import com.spotteacher.teacher.feature.teacher.domain.TeacherRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot

class CreateTeacherUseCaseTest : FunSpec({

    val teacherRepository = mockk<TeacherRepository>()
    val createTeacherUseCase = CreateTeacherUseCase(teacherRepository)

    test("should create teacher successfully") {
        // Given
        val schoolId = SchoolId(2L)
        val email = EmailAddress("test@example.com")
        val firstName = TeacherName("John")
        val lastName = TeacherName("Doe")
        val input = CreateTeacherUseCaseInput(
            schoolId = schoolId,
            email = email,
            firstName = firstName,
            lastName = lastName
        )
        val expectedTeacher = Teacher(
            id = TeacherId(100),
            schoolId = schoolId,
            firstName = firstName,
            lastName = lastName,
            email = email
        )

        val teacherSlot = slot<Teacher>()
        coEvery { teacherRepository.create(capture(teacherSlot)) } returns expectedTeacher

        // When
        val result = createTeacherUseCase.call(input)

        // Then
        result shouldBe expectedTeacher
        coVerify { teacherRepository.create(any()) }

        // Verify the captured teacher has the correct properties
        val capturedTeacher = teacherSlot.captured
        capturedTeacher.schoolId shouldBe schoolId
        capturedTeacher.firstName shouldBe firstName
        capturedTeacher.lastName shouldBe lastName
        capturedTeacher.email shouldBe email
    }
})
