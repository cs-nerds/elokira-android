package com.example.elokira.data

data class UserDetails(
    val admin: Boolean,
    val dateCreated: Long,
    val dateUpdated: Long,
    val firstName: String,
    val idNumber: String,
    val lastName: String,
    val lastUpdatedBy: String,
    val phoneNumber: String,
    val userId: String
)