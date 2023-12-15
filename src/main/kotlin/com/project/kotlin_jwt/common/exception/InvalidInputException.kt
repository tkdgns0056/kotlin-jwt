package com.project.kotlin_jwt.common.exception

class InvalidInputException (
    val fieldName: String = "",
    message: String = "Invalid Input"
) : RuntimeException(message)
