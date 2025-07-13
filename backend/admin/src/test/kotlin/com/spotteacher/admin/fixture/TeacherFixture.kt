package com.spotteacher.admin.fixture

import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.teacher.domain.Teacher
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import com.spotteacher.admin.feature.teacher.domain.TeacherName
import com.spotteacher.admin.feature.teacher.domain.TeacherRepository
import com.spotteacher.domain.EmailAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class TeacherFixture {

    @Autowired
    private lateinit var repository: TeacherRepository

    private var teacherIdCount = 1L

    fun build(
        id: TeacherId = TeacherId(teacherIdCount++),
        schoolId: SchoolId,
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

    suspend fun create(
        schoolId: SchoolId
    ): Teacher {
        val teacher = build(id = TeacherId(0),schoolId = schoolId)
        return repository.create(teacher)
    }
}
