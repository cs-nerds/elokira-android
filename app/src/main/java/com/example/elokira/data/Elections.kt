package com.example.elokira.data

import kotlinx.serialization.Serializable

@Serializable
data class Election(
    val electionId: String,
    val electionName: String,
    val StartDate: Long,
    val stopDate: Long,
    val createdBy: String,
    val dateModified: String
        )