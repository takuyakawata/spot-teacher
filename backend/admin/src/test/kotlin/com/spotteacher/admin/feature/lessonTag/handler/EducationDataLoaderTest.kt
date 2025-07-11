package com.spotteacher.admin.feature.lessonTag.handler

import com.spotteacher.admin.feature.lessonTag.domain.EducationName
import com.spotteacher.admin.feature.lessonTag.domain.EducationRepository
import com.spotteacher.admin.fixture.EducationFixture
import com.spotteacher.backend.DatabaseDescribeSpec
import graphql.GraphQLContext
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EducationDataLoaderTest(
    private val educationRepository: EducationRepository,
    private val educationFixture: EducationFixture,
) : DatabaseDescribeSpec({
    describe("EducationDataLoader") {
        val dataLoader = EducationDataLoader(educationRepository).getDataLoader(GraphQLContext.getDefault())
        
        describe("load") {
            val education = educationFixture.createEducation()

            context("when a valid id is given") {
                it("should return the education") {
                    dataLoader.load(education.id)
                    val result = dataLoader.dispatch().get(10L, TimeUnit.SECONDS)
                    result.size shouldBe 1
                    result[0].id shouldBe education.id.toGraphQLID()
                    result[0].name shouldBe education.name.value
                    result[0].isActive shouldBe education.isActive
                    result[0].displayOrder shouldBe education.displayOrder
                }
            }
        }

        describe("loadMany") {
            val education1 = educationFixture.createEducation()
            val education2 = educationFixture.createEducation(
                name = EducationName("Another Education")
            )

            context("when valid ids are given") {
                it("should return a list of educations") {
                    dataLoader.loadMany(listOf(education1.id, education2.id))
                    val result = dataLoader.dispatch().get(10L, TimeUnit.SECONDS)
                    result.size shouldBe 2
                    result[0].id shouldBe education1.id.toGraphQLID()
                    result[0].name shouldBe education1.name.value
                    result[1].id shouldBe education2.id.toGraphQLID()
                    result[1].name shouldBe education2.name.value
                }
            }
        }
    }
})
