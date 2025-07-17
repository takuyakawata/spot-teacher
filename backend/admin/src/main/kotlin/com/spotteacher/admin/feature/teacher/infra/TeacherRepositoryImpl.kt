package com.spotteacher.admin.feature.teacher.infra

import com.spotteacher.admin.feature.school.domain.SchoolId
import com.spotteacher.admin.feature.teacher.domain.Teacher
import com.spotteacher.admin.feature.teacher.domain.TeacherId
import com.spotteacher.admin.feature.teacher.domain.TeacherName
import com.spotteacher.admin.feature.teacher.domain.TeacherRepository
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.domain.EmailAddress
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.enums.UsersRole
import com.spotteacher.infra.db.tables.Teachers.Companion.TEACHERS
import com.spotteacher.infra.db.tables.records.TeachersRecord
import com.spotteacher.infra.db.tables.records.UsersRecord
import com.spotteacher.infra.db.tables.references.USERS
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.jetbrains.annotations.TestOnly
import org.springframework.stereotype.Repository
import java.util.UUID.randomUUID

@Repository
class TeacherRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
) : TeacherRepository {

    override suspend fun findById(id: TeacherId): Teacher? {
        val user = dslContext.get().nonBlockingFetchOne(
            USERS,
            USERS.ID.eq(id.value)
        ) ?: return null

        val teacher = dslContext.get().nonBlockingFetchOne(
            TEACHERS,
            TEACHERS.ID.eq(id.value)
        ) ?: return null

        return toEntity(user,teacher)
    }

    @TestOnly
    override suspend fun create(teacher: Teacher): Teacher {
        val userId = dslContext.get().insertInto(
            USERS,
            USERS.UUID,
            USERS.FIRST_NAME,
            USERS.LAST_NAME,
            USERS.EMAIL,
            USERS.ROLE,
        ).values(
            randomUUID().toString(),
            teacher.firstName.value,
            teacher.lastName.value,
            teacher.email.value,
            UsersRole.TEACHER,
        ).returning(USERS.ID).awaitFirstOrNull()?.id!!

        dslContext.get().insertInto(
            TEACHERS,
            TEACHERS.USER_ID,
            TEACHERS.SCHOOL_ID,
        ).values(
            userId,
            teacher.schoolId.value
        ).awaitLast()

        return teacher.copy(id = TeacherId(userId))
    }

    private fun toEntity(user: UsersRecord, teacher: TeachersRecord): Teacher {
        return Teacher(
            id = TeacherId(teacher.id!!),
            schoolId = SchoolId(teacher.schoolId),
            firstName = TeacherName(user.firstName),
            lastName = TeacherName(user.lastName),
            email = EmailAddress(user.email),
        )
    }
}
