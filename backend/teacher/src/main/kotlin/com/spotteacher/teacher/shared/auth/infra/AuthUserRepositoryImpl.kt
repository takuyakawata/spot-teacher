package com.spotteacher.teacher.shared.auth.infra

import com.spotteacher.domain.EmailAddress
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.tables.references.USERS
import com.spotteacher.infra.db.tables.references.USER_CREDENTIALS
import com.spotteacher.teacher.shared.auth.domain.AuthUser
import com.spotteacher.teacher.shared.auth.domain.AuthUserId
import com.spotteacher.teacher.shared.auth.domain.AuthUserRepository
import com.spotteacher.teacher.shared.domain.Password
import com.spotteacher.teacher.shared.infra.TransactionAwareDSLContext
import org.springframework.stereotype.Repository

@Repository
class AuthUserRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
) : AuthUserRepository {
    override suspend fun findByEmail(email: EmailAddress): AuthUser? {
        val user = dslContext.get().nonBlockingFetchOne(
            USERS,
            USERS.EMAIL.eq(email.value)
        ) ?: return null

        val credentials = dslContext.get().nonBlockingFetchOne(
            USER_CREDENTIALS,
            USER_CREDENTIALS.USER_ID.eq(user.id)
        ) ?: return null

        return AuthUser(
            id = AuthUserId(user.id!!),
            email = EmailAddress(user.email),
            password = Password(credentials.passwordHash),
        )
    }
    override suspend fun findByEmailAndActiveUser(email: EmailAddress): AuthUser? {
        return findByEmail(email)
    }
}
