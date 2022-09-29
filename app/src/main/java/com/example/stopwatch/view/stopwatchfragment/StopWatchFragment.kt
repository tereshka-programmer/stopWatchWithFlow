package com.example.stopwatch.view.stopwatchfragment

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.stopwatch.R
import com.example.stopwatch.databinding.StopWatchFragmentBinding
import com.example.stopwatch.model.TimeState
import kotlinx.coroutines.launch

class StopWatchFragment : Fragment(R.layout.stop_watch_fragment) {

    val viewModel by viewModels<StopWatchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = StopWatchFragmentBinding.inflate(inflater, container, false)
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            binding.tvStopWatch as TextView,
            1,
            300,
            1,
            TypedValue.COMPLEX_UNIT_DIP
        )

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.timeState.collect { timeState ->
                    binding.tvStopWatch.text = renderResult(timeState)
                }
            }
        }

        binding.tvStopWatch.setOnClickListener {
            if (viewModel.timeState.value.inProgress) {
                viewModel.stopTimeWatch()
                binding.tvStopWatch.setTextColor(resources.getColor(R.color.red))
            }
            else {
                viewModel.startTimeWatch()
                binding.tvStopWatch.setTextColor(resources.getColor(R.color.white))
            }
        }

        return binding.root
    }


    private fun renderResult(timeState: TimeState) : String{
        var sec: String = ""
        var min: String = ""

        if (timeState.seconds<10) sec = "0${timeState.seconds}" else sec=timeState.seconds.toString()
        if (timeState.minutes<10) min = "0${timeState.minutes}" else min=timeState.minutes.toString()

        return "$min:$sec"
    }

}