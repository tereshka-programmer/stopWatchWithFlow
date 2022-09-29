package com.example.stopwatch.view.stopwatchfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatch.model.TimeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StopWatchViewModel : ViewModel() {

    private val _timeState = MutableStateFlow(TimeState(0, 0))
    val timeState: StateFlow<TimeState> = _timeState.asStateFlow()

    private var inProgress = true

    init {
        viewModelScope.launch {
            timeWatcher(timeState.value).collect { time ->
                _timeState.value = time
            }
        }
    }

    fun startTimeWatch() {
        viewModelScope.launch {
            inProgress = true
            _timeState.value.inProgress = true
            timeWatcher(timeState.value).collect { time ->
                _timeState.value = time
            }
        }
    }

    fun stopTimeWatch() {
        inProgress = false
        _timeState.value.inProgress = false
    }

    private fun timeWatcher(timeState: TimeState): Flow<TimeState> = flow {

        var sec = timeState.seconds
        var min = timeState.minutes

        while (inProgress) {

            if (sec < 60) {
                delay(1000)
                sec++
                emit(TimeState(sec, min))
            } else {
                sec = 0
                min++
                emit(TimeState(sec, min))
            }

        }

    }.flowOn(Dispatchers.Default)

}