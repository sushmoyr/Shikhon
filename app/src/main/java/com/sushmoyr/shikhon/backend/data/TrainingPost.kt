package com.sushmoyr.shikhon.backend.data

data class TrainingPost(
    val postId: String = "",
    var user: User = User(),
    val trainingName: String = "",
    val trainingDescription: String = "",
    val trainingLocation: String = "",
    val photoUris: List<String> = emptyList()
)
