package com.spotteacher.admin.feature.adminUser.infra

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserId
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.feature.adminUser.domain.InActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.Password
import com.spotteacher.admin.shared.infra.TransactionAwareDSLContext
import com.spotteacher.domain.EmailAddress
import com.spotteacher.extension.nonBlockingFetch
import com.spotteacher.extension.nonBlockingFetchOne
import com.spotteacher.infra.db.enums.UsersRole
import com.spotteacher.infra.db.tables.AdminUsers.Companion.ADMIN_USERS
import com.spotteacher.infra.db.tables.Users.Companion.USERS
import com.spotteacher.infra.db.tables.records.AdminUsersRecord
import com.spotteacher.infra.db.tables.records.UsersRecord
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitLast
import org.jooq.Record
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class AdminUserRepositoryImpl(
    private val dslContext: TransactionAwareDSLContext
) : AdminUserRepository {
    override suspend fun getAll(): List<AdminUser> {
        val adminUsers = dslContext.get().nonBlockingFetch(ADMIN_USERS)

        return adminUsers.mapNotNull { adminUser ->
            val userRecord = dslContext.get().nonBlockingFetchOne(
                USERS,
                USERS.ID.eq(adminUser.userId)
            )

            userRecord?.let { toEntity(adminUser,it) }
        }
    }

    override suspend fun findById(id: AdminUserId): AdminUser? {
        // Get the admin user record
        val adminUser = dslContext.get().nonBlockingFetchOne(
            ADMIN_USERS,
            ADMIN_USERS.ID.eq(id.value)
        ) ?: return null

        // Get the corresponding user record
        val userRecord = dslContext.get().nonBlockingFetchOne(
            USERS,
            USERS.ID.eq(adminUser.userId)
        ) ?: return null

        return toEntity(adminUser, userRecord)
    }

    override suspend fun create(user: AdminUser) {
        // Insert into users table first
        val userRecord = when (user) {
            is ActiveAdminUser -> {
                val userId = dslContext.get().insertInto(
                    USERS,
                    USERS.UUID,
                    USERS.FIRST_NAME,
                    USERS.LAST_NAME,
                    USERS.EMAIL,
                    USERS.PASSWORD_HASH,
                    USERS.ROLE
                ).values(
                    UUID.randomUUID().toString(),
                    user.firstName.value,
                    user.lastName.value,
                    user.email.value,
                    user.password.value,
                    UsersRole.ADMIN
                ).returning(USERS.ID).awaitFirstOrNull()?.id!!

                // Then insert into admin_users table
                dslContext.get().insertInto(
                    ADMIN_USERS,
                    ADMIN_USERS.USER_ID
                ).values(
                    userId
                ).awaitLast()
            }
            is InActiveAdminUser -> {
                // For inactive users, we still need to create a user record
                // but with placeholder values for email and password
                val userId = dslContext.get().insertInto(
                    USERS,
                    USERS.UUID,
                    USERS.FIRST_NAME,
                    USERS.LAST_NAME,
                    USERS.EMAIL,
                    USERS.PASSWORD_HASH,
                    USERS.ROLE
                ).values(
                    UUID.randomUUID().toString(),
                    user.firstName.value,
                    user.lastName.value,
                    "inactive-${UUID.randomUUID()}@example.com", // Placeholder email
                    "inactive", // Placeholder password
                    UsersRole.ADMIN
                ).returning(USERS.ID).awaitFirstOrNull()?.id!!

                // Then insert into admin_users table
                dslContext.get().insertInto(
                    ADMIN_USERS,
                    ADMIN_USERS.USER_ID
                ).values(
                    userId
                ).awaitLast()
            }
        }
    }

    override suspend fun update(user: AdminUser) {
        when (user) {
            is ActiveAdminUser -> {
                // First find the user_id from admin_users table
                val adminUser = dslContext.get().nonBlockingFetchOne(
                    ADMIN_USERS,
                    ADMIN_USERS.ID.eq(user.id.value)
                )

                if (adminUser != null) {
                    val userId = adminUser.userId

                    // Update the users table
                    dslContext.get().update(USERS)
                        .set(USERS.FIRST_NAME, user.firstName.value)
                        .set(USERS.LAST_NAME, user.lastName.value)
                        .set(USERS.EMAIL, user.email.value)
                        .set(USERS.PASSWORD_HASH, user.password.value)
                        .where(USERS.ID.eq(userId))
                        .awaitLast()
                }
            }
            is InActiveAdminUser -> {
                // First find the user_id from admin_users table
                val adminUser = dslContext.get().nonBlockingFetchOne(
                    ADMIN_USERS,
                    ADMIN_USERS.ID.eq(user.id.value)
                )

                if (adminUser != null) {
                    val userId = adminUser.userId

                    // Update only the name fields in the users table
                    dslContext.get().update(USERS)
                        .set(USERS.FIRST_NAME, user.firstName.value)
                        .set(USERS.LAST_NAME, user.lastName.value)
                        .where(USERS.ID.eq(userId))
                        .awaitLast()
                }
            }
        }
    }

    override suspend fun delete(id: AdminUserId) {
        // First find the user_id from admin_users table
        val adminUser = dslContext.get().nonBlockingFetchOne(
            ADMIN_USERS,
            ADMIN_USERS.ID.eq(id.value)
        )

        if (adminUser != null) {
            val userId = adminUser.userId

            // Delete from admin_users table first (due to foreign key constraint)
            dslContext.get().deleteFrom(ADMIN_USERS)
                .where(ADMIN_USERS.ID.eq(id.value))
                .awaitLast()

            // Then delete from users table
            dslContext.get().deleteFrom(USERS)
                .where(USERS.ID.eq(userId))
                .awaitLast()
        }
    }

    override suspend fun findByEmailAndActiveUser(emailAddress: EmailAddress): ActiveAdminUser? {
        // Find the user by email
        val userRecord = dslContext.get().nonBlockingFetchOne(
            USERS,
            USERS.EMAIL.eq(emailAddress.value),
            USERS.ROLE.eq(UsersRole.ADMIN)
        ) ?: return null

        // Find the corresponding admin user
        val adminUser = dslContext.get().nonBlockingFetchOne(
            ADMIN_USERS,
            ADMIN_USERS.USER_ID.eq(userRecord.id)
        ) ?: return null

        // Create and return the admin user if it's active
        val user = toEntity(adminUser, userRecord)
        return if (user is ActiveAdminUser) user else null
    }

    private fun toEntity(adminUser: AdminUsersRecord, user: UsersRecord): AdminUser {
        // Check if this is an active user (has valid email)
        return if (user.email.contains("inactive-")) {
            // Inactive user
            InActiveAdminUser(
                id = AdminUserId(adminUser.id!!),
                firstName = AdminUserName(user.firstName),
                lastName = AdminUserName(user.lastName)
            )
        } else {
            // Active user
            ActiveAdminUser(
                id = AdminUserId(adminUser.id!!),
                firstName = AdminUserName(user.firstName),
                lastName = AdminUserName(user.lastName),
                email = EmailAddress(user.email),
                password = Password(user.passwordHash)
            )
        }
    }
}
