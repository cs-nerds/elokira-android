package com.example.elokira.data

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse (
    val firstName: String,
    val lastName: String,
    val idNumber: String,
    val verified: Boolean
        )