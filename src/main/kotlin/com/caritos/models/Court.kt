package com.caritos.models

import kotlinx.serialization.Serializable

@Serializable
data class Court(
    val id: Int,
    val name: String,
    val location: String
)
