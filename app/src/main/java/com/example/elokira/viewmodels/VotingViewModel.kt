package com.example.elokira.viewmodels

import ResultObserver
import androidx.lifecycle.ViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class VotingViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val PositionsResultEmitter = EventEmitter<ResultObserver>()
    val positionResult : EventSource<ResultObserver> get() = PositionsResultEmitter
}