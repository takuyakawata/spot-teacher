package com.spotteacher.admin.feature.lessonPlan.domain

import arrow.core.Nel
import com.spotteacher.admin.feature.company.domain.CompanyId
import com.spotteacher.admin.feature.product.domain.ProductErrorCode
import com.spotteacher.admin.feature.uploadFile.domain.UploadFileId
import com.spotteacher.util.Identity
import java.time.LocalDateTime
import java.time.LocalTime

sealed interface LessonPlan {
    val id: LessonPlanId
    val companyId: CompanyId
    val images: List<UploadFileId>
    val createdAt: LocalDateTime
}

data class PublishedLessonPlan(
    override val id: LessonPlanId,
    override val companyId: CompanyId,
    override val images: List<UploadFileId>,
    override val createdAt: LocalDateTime,
    val title: LessonPlanTitle,
    val description: LessonPlanDescription,
    val lessonType: LessonType,
    val location: LessonLocation,
    val annualMaxExecutions: Int,
    val lessonPlanDates: Nel<LessonPlanDate>
) : LessonPlan {
    companion object {
//        fun create(
//            companyId: CompanyId,
//            title: LessonPlanTitle,
//            description: LessonPlanDescription,
//            lessonType: LessonType,
//            location: LessonLocation,
//            annualMaxExecutions: Int,
//            lessonPlanDates: Nel<LessonPlanDate>
//        ) = PublishedLessonPlan(
//            id = LessonPlanId(0),
//            companyId = companyId,
//            images = emptyList(),
//            createdAt = LocalDateTime.now(),
//            title = title,
//            description = description,
//            lessonType = lessonType,
//            location = location,
//            annualMaxExecutions = annualMaxExecutions,
//            lessonPlanDates = lessonPlanDates
//        )
    }

    fun toDraftLessonPlan() = DraftLessonPlan(
        id = id,
        companyId = companyId,
        images = images,
        createdAt = createdAt,
        title = title,
        description = description,
        lessonType = lessonType,
        location = location,
        annualMaxExecutions = annualMaxExecutions,
        lessonPlanDates = lessonPlanDates
    )
}

data class DraftLessonPlan(
    override val id: LessonPlanId,
    override val companyId: CompanyId,
    override val images: List<UploadFileId>,
    override val createdAt: LocalDateTime,
    val title: LessonPlanTitle?,
    val description: LessonPlanDescription?,
    val lessonType: LessonType?,
    val location: LessonLocation?,
    val annualMaxExecutions: Int?,
    val lessonPlanDates: Nel<LessonPlanDate>?
) : LessonPlan {
    companion object {
        fun create(
            companyId: CompanyId,
            title: LessonPlanTitle?,
            description: LessonPlanDescription?,
            lessonType: LessonType?,
            location: LessonLocation?,
            annualMaxExecutions: Int?,
            lessonPlanDates: Nel<LessonPlanDate>?
        ) = DraftLessonPlan(
            id = LessonPlanId(0),
            companyId = companyId,
            images = emptyList(),
            createdAt = LocalDateTime.now(),
            title = title,
            location = location,
            description = description,
            lessonType = lessonType,
            annualMaxExecutions = annualMaxExecutions,
            lessonPlanDates = lessonPlanDates
        )
    }

    fun toPublishedLessonPlan() = PublishedLessonPlan(
        id = id,
        companyId = companyId,
        images = images,
        createdAt = createdAt,
        title = title!!,
        description = description!!,
        lessonType = lessonType!!,
        location = location!!,
        annualMaxExecutions = annualMaxExecutions!!,
        lessonPlanDates = lessonPlanDates!!
    )
}

class LessonPlanId(override val value: Long) : Identity<Long>(value)

@JvmInline
value class LessonPlanDescription(val value: String) {
    companion object {
        const val MAX_LENGTH = 1000
    }
    init {
        require(value.length <= MAX_LENGTH) { "Description must be less than $MAX_LENGTH characters" }
    }
}

@JvmInline
value class LessonPlanTitle(val value: String) {
    companion object {
        const val MAX_LENGTH = 100
    }
}

@JvmInline
value class LessonLocation(val value: String) {
    companion object {
        const val MAX_LENGTH = 100
    }
    init {
        require(value.length <= MAX_LENGTH) { "Location must be less than $MAX_LENGTH characters" }
    }
}

enum class LessonType {
    ONLINE, OFFLINE, ONLINE_AND_OFFLINE
}

data class LessonPlanDate(
    val startMonth: Int,
    val startDay: Int,
    val endMonth: Int,
    val endDay: Int,
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    companion object {
        private const val MAX_DATE = 31
        private const val MINI_DATE = 1
        private const val MAX_MONTH = 12
        private const val MINI_MONTH = 1
    }
    init {
        // TODO 他にロジックありそう？
        require(startMonth in MINI_MONTH..MAX_MONTH) { "Start month must be between $MINI_MONTH and $MAX_MONTH" }
        require(endMonth in MINI_MONTH..MAX_MONTH) { "End month must be between $MINI_MONTH and $MAX_MONTH" }
        require(startMonth <= endMonth) { "Start month must be less than or equal to end month" }
        require(startTime.isBefore(endTime)) { "Start time must be before end time" }
        require(startDay in MINI_DATE..MAX_DATE) { "Start day must be between $MINI_DATE and $MAX_DATE" }
        require(endDay in MINI_DATE..MAX_DATE) { "End day must be between $MINI_DATE and $MAX_DATE" }
    }
}

data class LessonPlanError(
    val code: ProductErrorCode,
    val message: String
)

enum class LessonPlanErrorCode {
    LESSON_PLAN_NOT_FOUND,
    LESSON_PLAN_ALREADY_EXISTS,
}
