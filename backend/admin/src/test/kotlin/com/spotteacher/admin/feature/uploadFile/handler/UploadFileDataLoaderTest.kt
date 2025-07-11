package com.spotteacher.admin.feature.uploadFile.handler

import com.spotteacher.admin.feature.uploadFile.domain.FileKey
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileRepository
import com.spotteacher.admin.fixture.UploadFileFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import graphql.GraphQLContext
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UploadFileDataLoaderTest(
    private val repository: UploadFileRepository,
    private val uploadFileFixture: UploadFileFixture
) : DatabaseDescribeSpec({
    describe("UploadFileDataLoader") {
        val dataLoader = UploadFileDataLoader(repository).getDataLoader(GraphQLContext.getDefault())
        
        describe("load") {
            val uploadFile = uploadFileFixture.createWithFileKey(
                fileKey = FileKey("test/file-key.jpg")
            )

            context("when a valid id is given") {
                it("should return the upload file") {
                    dataLoader.load(uploadFile.id)
                    val result = dataLoader.dispatch().get(10L, TimeUnit.SECONDS)
                    result.size shouldBe 1
                    result[0]?.id shouldBe uploadFile.id.toGraphQLId()
                    result[0]?.fileKey shouldBe uploadFile.fileKey.value
                    result[0]?.uploadStatus shouldBe uploadFile.status
                }
            }
        }

        describe("loadMany") {
            val uploadFile1 = uploadFileFixture.createWithFileKey(
                fileKey = FileKey("test/file-key-1.jpg")
            )
            val uploadFile2 = uploadFileFixture.createWithFileKey(
                fileKey = FileKey("test/file-key-2.jpg")
            )

            context("when valid ids are given") {
                it("should return a list of upload files") {
                    dataLoader.loadMany(listOf(uploadFile1.id, uploadFile2.id))
                    val result = dataLoader.dispatch().get(10L, TimeUnit.SECONDS)
                    result.size shouldBe 2
                    result[0]?.id shouldBe uploadFile1.id.toGraphQLId()
                    result[0]?.fileKey shouldBe uploadFile1.fileKey.value
                    result[1]?.id shouldBe uploadFile2.id.toGraphQLId()
                    result[1]?.fileKey shouldBe uploadFile2.fileKey.value
                }
            }

            context("when some ids don't exist") {
                val nonExistentId = UploadFileId(999L)
                it("should return null for non-existent files") {
                    dataLoader.loadMany(listOf(uploadFile1.id, nonExistentId))
                    val result = dataLoader.dispatch().get(10L, TimeUnit.SECONDS)
                    result.size shouldBe 2
                    result[0]?.id shouldBe uploadFile1.id.toGraphQLId()
                    result[1] shouldBe null
                }
            }
        }
    }
})
