package com.example.testoftest.dashboard.pojo

import java.io.Serializable

data class Analytics(
    val onclick: Onclick,
    val onload: Onload,
    val onsent: Onsent
): Serializable