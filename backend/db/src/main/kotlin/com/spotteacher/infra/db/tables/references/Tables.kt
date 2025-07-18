@file:Suppress("warnings")
/*
 * This file is generated by jOOQ.
 */
package com.spotteacher.infra.db.tables.references


import com.spotteacher.infra.db.tables.AdminUsers
import com.spotteacher.infra.db.tables.Companies
import com.spotteacher.infra.db.tables.CompanyMembers
import com.spotteacher.infra.db.tables.Educations
import com.spotteacher.infra.db.tables.LessonPlanDates
import com.spotteacher.infra.db.tables.LessonPlanGrades
import com.spotteacher.infra.db.tables.LessonPlanSubjects
import com.spotteacher.infra.db.tables.LessonPlans
import com.spotteacher.infra.db.tables.LessonPlansEducations
import com.spotteacher.infra.db.tables.LessonReservationDates
import com.spotteacher.infra.db.tables.LessonReservationEducations
import com.spotteacher.infra.db.tables.LessonReservationGrades
import com.spotteacher.infra.db.tables.LessonReservationSubjects
import com.spotteacher.infra.db.tables.LessonReservations
import com.spotteacher.infra.db.tables.LessonScheduleCancels
import com.spotteacher.infra.db.tables.LessonScheduleEducations
import com.spotteacher.infra.db.tables.LessonScheduleGrades
import com.spotteacher.infra.db.tables.LessonScheduleReports
import com.spotteacher.infra.db.tables.LessonScheduleSubjects
import com.spotteacher.infra.db.tables.LessonSchedules
import com.spotteacher.infra.db.tables.Products
import com.spotteacher.infra.db.tables.RefreshTokens
import com.spotteacher.infra.db.tables.Schools
import com.spotteacher.infra.db.tables.Teachers
import com.spotteacher.infra.db.tables.UploadFiles
import com.spotteacher.infra.db.tables.UserCredentials
import com.spotteacher.infra.db.tables.Users



/**
 * The table <code>admin_users</code>.
 */
val ADMIN_USERS: AdminUsers = AdminUsers.ADMIN_USERS

/**
 * The table <code>companies</code>.
 */
val COMPANIES: Companies = Companies.COMPANIES

/**
 * CompanyMemberロールを持つユーザーの追加情報
 */
val COMPANY_MEMBERS: CompanyMembers = CompanyMembers.COMPANY_MEMBERS

/**
 * The table <code>educations</code>.
 */
val EDUCATIONS: Educations = Educations.EDUCATIONS

/**
 * 授業計画の日付
 */
val LESSON_PLAN_DATES: LessonPlanDates = LessonPlanDates.LESSON_PLAN_DATES

/**
 * The table <code>lesson_plan_grades</code>.
 */
val LESSON_PLAN_GRADES: LessonPlanGrades = LessonPlanGrades.LESSON_PLAN_GRADES

/**
 * The table <code>lesson_plan_subjects</code>.
 */
val LESSON_PLAN_SUBJECTS: LessonPlanSubjects = LessonPlanSubjects.LESSON_PLAN_SUBJECTS

/**
 * The table <code>lesson_plans</code>.
 */
val LESSON_PLANS: LessonPlans = LessonPlans.LESSON_PLANS

/**
 * The table <code>lesson_plans_educations</code>.
 */
val LESSON_PLANS_EDUCATIONS: LessonPlansEducations = LessonPlansEducations.LESSON_PLANS_EDUCATIONS

/**
 * 授業予約の日付
 */
val LESSON_RESERVATION_DATES: LessonReservationDates = LessonReservationDates.LESSON_RESERVATION_DATES

/**
 * The table <code>lesson_reservation_educations</code>.
 */
val LESSON_RESERVATION_EDUCATIONS: LessonReservationEducations = LessonReservationEducations.LESSON_RESERVATION_EDUCATIONS

/**
 * The table <code>lesson_reservation_grades</code>.
 */
val LESSON_RESERVATION_GRADES: LessonReservationGrades = LessonReservationGrades.LESSON_RESERVATION_GRADES

/**
 * The table <code>lesson_reservation_subjects</code>.
 */
val LESSON_RESERVATION_SUBJECTS: LessonReservationSubjects = LessonReservationSubjects.LESSON_RESERVATION_SUBJECTS

/**
 * The table <code>lesson_reservations</code>.
 */
val LESSON_RESERVATIONS: LessonReservations = LessonReservations.LESSON_RESERVATIONS

/**
 * The table <code>lesson_schedule_cancels</code>.
 */
val LESSON_SCHEDULE_CANCELS: LessonScheduleCancels = LessonScheduleCancels.LESSON_SCHEDULE_CANCELS

/**
 * The table <code>lesson_schedule_educations</code>.
 */
val LESSON_SCHEDULE_EDUCATIONS: LessonScheduleEducations = LessonScheduleEducations.LESSON_SCHEDULE_EDUCATIONS

/**
 * The table <code>lesson_schedule_grades</code>.
 */
val LESSON_SCHEDULE_GRADES: LessonScheduleGrades = LessonScheduleGrades.LESSON_SCHEDULE_GRADES

/**
 * The table <code>lesson_schedule_reports</code>.
 */
val LESSON_SCHEDULE_REPORTS: LessonScheduleReports = LessonScheduleReports.LESSON_SCHEDULE_REPORTS

/**
 * The table <code>lesson_schedule_subjects</code>.
 */
val LESSON_SCHEDULE_SUBJECTS: LessonScheduleSubjects = LessonScheduleSubjects.LESSON_SCHEDULE_SUBJECTS

/**
 * The table <code>lesson_schedules</code>.
 */
val LESSON_SCHEDULES: LessonSchedules = LessonSchedules.LESSON_SCHEDULES

/**
 * The table <code>products</code>.
 */
val PRODUCTS: Products = Products.PRODUCTS

/**
 * The table <code>refresh_tokens</code>.
 */
val REFRESH_TOKENS: RefreshTokens = RefreshTokens.REFRESH_TOKENS

/**
 * The table <code>schools</code>.
 */
val SCHOOLS: Schools = Schools.SCHOOLS

/**
 * Teacherロールを持つユーザーの情報
 */
val TEACHERS: Teachers = Teachers.TEACHERS

/**
 * The table <code>UPLOAD_FILES</code>.
 */
val UPLOAD_FILES: UploadFiles = UploadFiles.UPLOAD_FILES

/**
 * The table <code>user_credentials</code>.
 */
val USER_CREDENTIALS: UserCredentials = UserCredentials.USER_CREDENTIALS

/**
 * 全ユーザーの共通情報を格納する基盤テーブル
 */
val USERS: Users = Users.USERS
