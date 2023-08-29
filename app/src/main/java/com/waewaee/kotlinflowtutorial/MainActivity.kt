package com.waewaee.kotlinflowtutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waewaee.kotlinflowtutorial.ui.theme.KotlinFlowTutorialTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.stateFlow.collectLatest { number ->
//                    binding.tvCounter.text = number.toString()
//                }
//            }
//        }

        collectLatestLifecycleFlow(viewModel.stateFlow) { number ->
//           binding.tvCounter.text = number.toString()
        }

        setContent {
            KotlinFlowTutorialTheme {

                val viewModel = viewModel<MainViewModel>()
                val count = viewModel.stateFlow.collectAsState(initial = 10)

                LaunchedEffect(key1 = true) {
                    viewModel.sharedFlow.collect { number ->
                        ////
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    Button(onClick = { viewModel.incrementCounter() }) {
                        Text(text = "Counter: ${count.value}")
                    }
                }
            }
        }
    }
}

fun <T> ComponentActivity.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> ComponentActivity.collectLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}