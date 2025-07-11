package com.spotteacher.admin.feature.uploadFile.handler

import arrow.core.nonEmptyListOf
import com.spotteacher.admin.feature.uploadFile.domain.FileKey
import com.spotteacher.admin.fixture.UploadFileFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import com.spotteacher.backend.graphql.graphQLBuilder
import com.spotteacher.backend.graphql.postGraphQLRequest
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class DeleteUploadFilesMutationTest(
    private val client: WebTestClient,
    private val uploadFileFixture: UploadFileFixture,
) : DatabaseDescribeSpec({
    suspend fun generateQuery(input: DeleteUploadFilesMutationInput): String {
        return graphQLBuilder {
            DeleteUploadFilesMutation::deleteUploadFiles.asMutation(input)
        }
    }

    describe("delete upload files mutation") {
        val uploadFile1 = uploadFileFixture.createWithFileKey(
            fileKey = FileKey("test/file-key-1.jpg")
        )

        context("when valid input is given") {
            it("should delete the upload files") {
                val input = DeleteUploadFilesMutationInput(
                    uploadFileIDs = nonEmptyListOf(uploadFile1.id.toGraphQLId())
                )
                client.postGraphQLRequest(generateQuery(input))
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.errors").doesNotExist()
            }
        }

        context("when invalid input is given") {
            it("should return error message") {
                val input = DeleteUploadFilesMutationInput(
                    uploadFileIDs = emptyList()
                )
                client.postGraphQLRequest(generateQuery(input))
                    .expectStatus().isOk
                    .expectBody()
                    .jsonPath("$.errors[0].message").value<String> { message ->
                        message shouldBe "uploadFileIDs must not be empty"
                    }
            }
        }
    }
})
