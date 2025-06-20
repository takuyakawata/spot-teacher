package com.spotteacher.admin.feature.lessonTag.domain

import com.spotteacher.util.Identity


/**
 * OO教育のタグ情報
 *
 * Represents an educational construct with a unique identifier, a name, and an optional description.
 *
 * @property id The unique identifier for the education instance.
 * @property name The name representing the education instance.
 */
data class Education(
    val id: EducationId,
    val name: EducationName,
    val isActive: Boolean = false,
    val displayOrder: Int = 0,
){
    companion object{
        fun create(
            name: EducationName,
        ) = Education(
            id = EducationId(0),
            name = name,
        )
    }

    fun update(
        name: EducationName?,
        isActive: Boolean?,
        displayOrder: Int?
    ) = Education(
        id = this.id,
        name = name ?: this.name,
        isActive = isActive ?: this.isActive,
        displayOrder = displayOrder ?: this.displayOrder,
    )
}



class EducationId(override val value: Long): Identity<Long>(value)

@JvmInline
value class EducationName(val value: String)

data class EducationError(
    val message: String,
    val code: EducationErrorCode
)

enum class EducationErrorCode {
    EDUCATION_NOT_FOUND,
    EDUCATION_ALREADY_EXISTS,
}
