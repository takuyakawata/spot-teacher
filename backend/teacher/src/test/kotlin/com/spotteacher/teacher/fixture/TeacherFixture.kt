package com.spotteacher.teacher.fixture

import com.spotteacher.domain.EmailAddress
import com.spotteacher.teacher.feature.school.domain.SchoolId
import com.spotteacher.teacher.feature.teacher.domain.Teacher
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import com.spotteacher.teacher.feature.teacher.domain.TeacherName
import com.spotteacher.teacher.feature.teacher.domain.TeacherRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TeacherFixture {

    @Autowired
    private lateinit var repository: TeacherRepository

    private var teacherIdCount = 1L

    fun build(
        id: TeacherId = TeacherId(teacherIdCount++),
        schoolId: SchoolId = SchoolId(1),
        firstName: String = "Test",
        lastName: String = "Teacher",
        email: String = "test.teacher${teacherIdCount}@example.com"
    ): Teacher {
        return Teacher(
            id = id,
            schoolId = schoolId,
            firstName = TeacherName(firstName),
            lastName = TeacherName(lastName),
            email = EmailAddress(email)
        )
    }

    suspend fun create(): Teacher {
        val teacher = build(id = TeacherId(0))
        return repository.create(teacher)
    }
}
