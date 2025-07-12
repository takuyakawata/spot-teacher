//package com.spotteacher.teacher.feature.lessonReservation.usecase
//
//import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservation
//import com.spotteacher.teacher.feature.lessonReservation.domain.LessonReservationRepository
//import com.spotteacher.usecase.UseCase
//
//@UseCase
//class CreateLessonReservationUseCase(
//    private val lessonReservationRepository: LessonReservationRepository
//){
//    suspend fun call(){
//        val lessonReservation = LessonReservation.create(
//            TODO()
//        )
//
//        //永続化
//        lessonReservationRepository.create(lessonReservation)
//
//    }
//}