package com.example.stopwatch.model

data class TimeState(
    var seconds: Int,
    var minutes: Int,
    var inProgress: Boolean = true
)
