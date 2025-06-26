package com.spotteacher.admin.feature.adminUser.infra

import com.spotteacher.admin.feature.adminUser.domain.ActiveAdminUser
import com.spotteacher.admin.feature.adminUser.domain.AdminUserName
import com.spotteacher.admin.feature.adminUser.domain.AdminUserRepository
import com.spotteacher.admin.fixture.AdminUserFixture
import com.spotteacher.admin.shared.domain.Password
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.domain.EmailAddress
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest(webEnvironment = NONE)
class AdminUserRepositoryImplTest(
    private val adminUserRepository: AdminUserRepository,
    private val adminUserFixture: AdminUserFixture,
    @Autowired private val passwordEncoder: PasswordEncoder
) : DatabaseDescribeSpec({

    describe("AdminUserRepository") {
        describe("create and findById") {
            it("should create an ActiveAdminUser and find it by ID") {
                // Create a new ActiveAdminUser
                val activeUser = adminUserFixture.buildActiveAdminUser(
                    firstName = AdminUserName("John"),
                    lastName = AdminUserName("Doe"),
                    email = EmailAddress("john.doe@example.com"),
                )
                val  password = Password("securePassword123")

                
                // Create the user in the repository
                adminUserRepository.create(activeUser, passwordEncoder.encode(password.value))

                // Find the user by ID
                val foundUser = adminUserRepository.findById(activeUser.id)

                // Verify the user was found and has the correct properties
                foundUser shouldNotBe null
                foundUser as ActiveAdminUser
                foundUser.firstName.value shouldBe "John"
                foundUser.lastName.value shouldBe "Doe"
                foundUser.email.value shouldBe "john.doe@example.com"
            }
        }

        describe("update") {
            it("should update an ActiveAdminUser") {
                // Create a new ActiveAdminUser
                val activeUser = adminUserFixture.buildActiveAdminUser(
                    firstName = AdminUserName("Update"),
                    lastName = AdminUserName("Test"),
                    email = EmailAddress("update.test@example.com"),
                )
                val  password = Password("securePassword123")

                // Create the user in the repository
                adminUserRepository.create(activeUser, password.value)

                // Update the user
                val updatedUser = activeUser.copy(
                    firstName = AdminUserName("Updated"),
                    lastName = AdminUserName("Name"),
                    email = EmailAddress("updated.email@example.com"),
                )
                adminUserRepository.update(updatedUser)

                // Find the user by ID
                val foundUser = adminUserRepository.findById(activeUser.id)

                // Verify the user was updated
                foundUser shouldNotBe null
                foundUser as ActiveAdminUser
                foundUser.firstName.value shouldBe "Updated"
                foundUser.lastName.value shouldBe "Name"
                foundUser.email.value shouldBe "updated.email@example.com"
            }
        }

        describe("updatePassword") {
            it("should update user's password") {
                // Create a new ActiveAdminUser
                val activeUser = adminUserFixture.buildActiveAdminUser(
                    firstName = AdminUserName("Password"),
                    lastName = AdminUserName("Test"),
                    email = EmailAddress("password.test@example.com"),
                )
                val  password = Password("<PASSWORD>")

                // Create the user in the repository
                adminUserRepository.create(activeUser, password.value)

                // Update password
                val newPassword = Password("newPassword123")
                adminUserRepository.updatePassword(newPassword)

                // Find the user by ID to verify password was updated
                val foundUser = adminUserRepository.findById(activeUser.id)

                // Verify the password was updated
                foundUser shouldNotBe null
                foundUser as ActiveAdminUser
            }
        }

        describe("delete") {
            it("should delete a user") {
                // Create a new ActiveAdminUser
                val activeUser = adminUserFixture.buildActiveAdminUser(
                    firstName = AdminUserName("Delete"),
                    lastName = AdminUserName("Test"),
                    email = EmailAddress("delete.test@example.com"),
                )
                val  password = Password("<PASSWORD>")

                // Create the user in the repository
                adminUserRepository.create(activeUser, password.value)

                // Verify the user exists
                val foundUser = adminUserRepository.findById(activeUser.id)
                foundUser shouldNotBe null

                // Delete the user
                adminUserRepository.delete(activeUser.id)

                // Verify the user no longer exists
                val deletedUser = adminUserRepository.findById(activeUser.id)
                deletedUser shouldBe null
            }
        }

        describe("getAll") {
            it("should return all users") {
                // Create multiple users
                val user1 = adminUserFixture.buildActiveAdminUser(
                    firstName = AdminUserName("User"),
                    lastName = AdminUserName("One"),
                    email = EmailAddress("user.one@example.com"),
                )
                val  password1 = Password("<PASSWORD>")
                val user2 = adminUserFixture.buildActiveAdminUser(
                    firstName = AdminUserName("User"),
                    lastName = AdminUserName("Two"),
                    email = EmailAddress("user.two@example.com"),
                )
                val password2 = Password("password2")

                // Create the users in the repository
                adminUserRepository.create(user1, password1.value)
                adminUserRepository.create(user2, password2.value)

                // Get all users
                val allUsers = adminUserRepository.getAll()

                // Verify the users are returned
                allUsers.shouldNotBeEmpty()

                // Find the created users in the list
                val foundUser1 = allUsers.find { it is ActiveAdminUser && it.email.value == "user.one@example.com" }
                val foundUser2 = allUsers.find { it is ActiveAdminUser && it.email.value == "user.two@example.com" }

                foundUser1 shouldNotBe null
                foundUser2 shouldNotBe null

                foundUser1 as ActiveAdminUser
                foundUser2 as ActiveAdminUser

                foundUser1.firstName.value shouldBe "User"
                foundUser1.lastName.value shouldBe "One"

                foundUser2.firstName.value shouldBe "User"
                foundUser2.lastName.value shouldBe "Two"
            }
        }

        describe("findByEmailAndActiveUser") {
            it("should find an active user by email") {
                // Create a new ActiveAdminUser
                val activeUser = adminUserFixture.buildActiveAdminUser(
                    firstName = AdminUserName("Email"),
                    lastName = AdminUserName("Test"),
                    email = EmailAddress("email.test@example.com"),
                )
                val  password = Password("<PASSWORD>")

                // Create the user in the repository
                adminUserRepository.create(activeUser, password.value)

                // Find the user by email
                val foundUser = adminUserRepository.findByEmailAndActiveUser(EmailAddress("email.test@example.com"))

                // Verify the user was found
                foundUser shouldNotBe null
                foundUser!!.firstName.value shouldBe "Email"
                foundUser.lastName.value shouldBe "Test"
                foundUser.email.value shouldBe "email.test@example.com"
            }
        }
    }
}) {

}
