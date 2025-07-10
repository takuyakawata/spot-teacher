package com.spotteacher.teacher.feature.teacher.infra

import com.spotteacher.domain.EmailAddress
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.tables.Teachers.Companion.TEACHERS
import com.spotteacher.infra.db.tables.records.TeachersRecord
import com.spotteacher.infra.db.tables.references.USERS
import com.spotteacher.teacher.feature.teacher.domain.ActiveTeacher
import com.spotteacher.teacher.feature.teacher.domain.Teacher
import com.spotteacher.teacher.feature.teacher.domain.TeacherId
import com.spotteacher.teacher.feature.teacher.domain.TeacherRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.springframework.stereotype.Repository

@Repository
class TeacherRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
) : TeacherRepository {

    override suspend fun getAll(): List<Teacher> {
        val teachers = dslContext.get().nonBlockingFetch(TEACHERS)
        return teachers.map { toEntity(it) }
    }

    override suspend fun findById(id: TeacherId): Teacher? {
        val teacher = dslContext.get().nonBlockingFetchOne(
            TEACHERS,
            TEACHERS.ID.eq(id.value)
        ) ?: return null

        return toEntity(teacher)
    }

    override suspend fun findByUserId(userId: Long): Teacher? {
        val teacher = dslContext.get().nonBlockingFetchOne(
            TEACHERS,
            TEACHERS.USER_ID.eq(userId)
        ) ?: return null

        return toEntity(teacher)
    }

    override suspend fun create(teacher: ActiveTeacher): ActiveTeacher {
        val teacherId = dslContext.get().insertInto(
            TEACHERS,
            TEACHERS.USER_ID,
            TEACHERS.SCHOOL_ID
        ).values(
            teacher.userId,
            teacher.schoolId
        ).returning(TEACHERS.ID).awaitFirstOrNull()?.id!!

        return teacher.copy(id = TeacherId(teacherId))
    }

    override suspend fun update(teacher: ActiveTeacher) {
        dslContext.get().update(TEACHERS)
            .set(TEACHERS.USER_ID, teacher.userId)
            .set(TEACHERS.SCHOOL_ID, teacher.schoolId)
            .where(TEACHERS.ID.eq(teacher.id.value))
            .awaitLast()
    }

    override suspend fun delete(id: TeacherId) {
        dslContext.get().deleteFrom(TEACHERS)
            .where(TEACHERS.ID.eq(id.value))
            .awaitLast()
    }

    override suspend fun findBySchoolId(schoolId: Long): List<Teacher> {
        val teachers = dslContext.get().nonBlockingFetch(
            TEACHERS,
            TEACHERS.SCHOOL_ID.eq(schoolId)
        )
        return teachers.map { toEntity(it) }
    }

    override suspend fun findByEmail(email: EmailAddress): Teacher? {
        val teacher = dslContext.get().nonBlockingFetchOne(
            USERS,
            USERS.EMAIL.eq(email.value)
        ) ?: return null

        val teacherRecord = dslContext.get().nonBlockingFetchOne(
            TEACHERS,
            TEACHERS.USER_ID.eq(teacher.id)
        )

        return teacherRecord?.let { toEntity(it) }
    }

    private fun toEntity(teacher: TeachersRecord): ActiveTeacher {
        return ActiveTeacher(
            id = TeacherId(teacher.id!!),
            userId = teacher.userId,
            schoolId = teacher.schoolId
        )
    }
}
