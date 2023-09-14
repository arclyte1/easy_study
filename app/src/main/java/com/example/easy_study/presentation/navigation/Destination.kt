package com.example.easy_study.presentation.navigation

sealed class Destination(protected val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    object LoginScreen : NoArgumentsDestination("login")

    object RegistrationScreen : NoArgumentsDestination("registration")

    object GroupListScreen : NoArgumentsDestination("group_list")

    object GroupDetailsScreen : Destination("group_details", "groupId") {
        const val GROUP_ID_KEY = "groupId"

        operator fun invoke(groupId: Int): String = route.appendParams(
            GROUP_ID_KEY to groupId
        )
    }

    object LessonListScreen : Destination("lesson_list", "groupId") {
        const val GROUP_ID_KEY = "groupId"

        operator fun invoke(groupId: Int): String = route.appendParams(
            GROUP_ID_KEY to groupId
        )
    }

    object LessonActivityScreen : Destination("lesson_activity", "lessonId") {
        const val LESSON_ID_KEY = "lessonId"

        operator fun invoke(groupId: Int): String = route.appendParams(
            LESSON_ID_KEY to groupId
        )
    }

//    object StudentDetailsScreen : Destination("student_details")
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}