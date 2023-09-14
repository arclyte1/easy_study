package com.example.easy_study.domain.model

import com.example.easy_study.R

enum class UserRole {
    STUDENT, TEACHER;

    companion object {
        fun getValue(shortValue: String): UserRole = when (shortValue) {
            "ST" -> STUDENT
            "TR" -> TEACHER
            else -> STUDENT
        }

        fun toString(value: UserRole): String = when (value) {
            STUDENT -> "ST"
            TEACHER -> "TR"
        }

        fun getRes(value: UserRole): Int = when(value) {
            STUDENT -> R.string.student
            TEACHER -> R.string.teacher
        }
    }
}
//
//object UserRole {
//
//    enum class Role{
//        STUDENT,
//        TEACHER;
//    }
//
//    fun getValue(shortValue: String): Role = when (shortValue) {
//        "ST" -> Role.STUDENT
//        "TR" -> Role.TEACHER
//        else -> Role.STUDENT
//    }
//
//    fun getValue(context: Context, value: String): Role = when (value) {
//        context.resources.getString(R.string.student) -> Role.STUDENT
//        context.resources.getString(R.string.teacher) -> Role.TEACHER
//        else -> Role.STUDENT
//    }
//
//    fun toString(value: Role): String = when (value) {
//        Role.STUDENT -> "ST"
//        Role.TEACHER -> "TR"
//    }
//
//    fun getRes(value: Role): Int = when(value) {
//        Role.STUDENT -> R.string.student
//        Role.TEACHER -> R.string.teacher
//    }
//}

