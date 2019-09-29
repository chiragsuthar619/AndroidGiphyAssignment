package com.example.testoftest.dashboard.pojo

data class UserInfomodel(

    val rows: ArrayList<Row>,
    val title: String
)

data class Row(

    val description: String? = null,
    val imageHref: String? = null,
    val title: String? = null
)