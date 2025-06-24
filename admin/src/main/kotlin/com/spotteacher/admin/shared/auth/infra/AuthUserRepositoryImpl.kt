package com.spotteacher.admin.shared.auth.infra

import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.admin.feature.adminUser.handler.AdminUserType
import com.spotteacher.admin.shared.auth.domain.AuthUser
import com.spotteacher.admin.shared.auth.domain.AuthUserRepository
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.domain.EmailAddress
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.enums.UsersRole
import com.spotteacher.infra.db.tables.references.USERS
import com.spotteacher.infra.db.tables.references.USER_CREDENTIALS
import org.springframework.stereotype.Repository

@Repository
class AuthUserRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
): AuthUserRepository {
    override suspend fun findByEmail(email: EmailAddress): AuthUser? {
        val user = dslContext.get().nonBlockingFetchOne(
            USERS,
            USERS.EMAIL.eq(email.value),
            USERS.ROLE.eq(UsersRole.ADMIN)
        )

        val credentials = dslContext.get().nonBlockingFetchOne(
            USER_CREDENTIALS,
            USER_CREDENTIALS.USER_ID.eq(user!!.id)
        )
        return if (credentials != null) {
            AuthUser(
                userId = AdminUserId(user.id!!),
                email = EmailAddress(user.email),
                password = Password(credentials.passwordHash),
            )
        } else {
            null
        }
    }
}
