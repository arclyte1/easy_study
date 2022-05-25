package com.example.easy_study.data.model

import com.example.easy_study.R

object UserRole {

    enum class Role{
        STUDENT,
        TEACHER;
    }

    fun getValue(value: String): Role = when (value) {
        "ST" -> Role.STUDENT
        "TR" -> Role.TEACHER
        else -> Role.STUDENT
    }

    fun getValue(value: Int): Role = when (value) {
        R.string.student -> Role.STUDENT
        R.string.teacher -> Role.TEACHER
        else -> Role.STUDENT
    }

    fun toString(value: Role): String = when (value) {
        Role.STUDENT -> "ST"
        Role.TEACHER -> "TR"
    }

    fun getRes(value: Role): Int = when(value) {
        Role.STUDENT -> R.string.student
        Role.TEACHER -> R.string.teacher
    }
}

