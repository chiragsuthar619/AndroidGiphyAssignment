package com.example.testoftest.dashboard.pojo

import java.io.Serializable

data class FixedHeightDownsampled(
    val height: String,
    val size: String,
    val url: String,
    val webp: String,
    val webp_size: String,
    val width: String
): Serializable