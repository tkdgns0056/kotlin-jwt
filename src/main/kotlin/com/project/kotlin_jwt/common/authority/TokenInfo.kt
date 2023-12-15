package com.project.kotlin_jwt.common.authority

data class TokenInfo (
    val grantType: String,
    val accessToken: String,
)