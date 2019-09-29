package com.example.testoftest.storage.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class GifInfo(
    @Id var id: Long = 0,
    var gif_id: String? = null,
    var up_vote_count: Long? = null,
    var down_vote_count: Long? = null
)