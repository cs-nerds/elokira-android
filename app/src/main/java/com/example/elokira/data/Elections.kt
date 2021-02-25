package com.example.elokira.data

import kotlinx.serialization.Serializable

@Serializable
data class Election(
    val electionId: String,
    val electionName: String,
    val StartDate: String,
    val stopDate: String,
    val createdBy: String,
    val dateModified: String
        )