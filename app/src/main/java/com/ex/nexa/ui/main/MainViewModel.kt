package com.ex.nexa.ui.main

import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel() {
    enum class WorkState {
        EMPTY,
        COMPARE,
        MATCH,
        UN_MATCH,
    }

    private val workState = MutableLiveData<WorkState>()
    private var masterKey: String = ""

    init {
        workState.value = WorkState.EMPTY
    }

    @UiThread
    fun getWorkState(): LiveData<WorkState> = workState

    @UiThread
    fun initWorks() {
        workState.postValue(WorkState.EMPTY)
        masterKey = ""
    }


    @UiThread
    fun onEditCommit(inputString: String) {
        val key = inputString.toUpperCase(Locale.ROOT).trim()
        when (workState.value) {
            // master input
            WorkState.EMPTY, WorkState.MATCH -> {
                if (inputString != "") {
                    masterKey = key
                    workState.value = WorkState.COMPARE
                }
            }
            // compare target input
            WorkState.COMPARE, WorkState.UN_MATCH -> {
                compareInput(key)
            }
            else -> {
            }
        }
    }

    @UiThread
    private fun compareInput(key: String) {
        if (key == masterKey) {
            workState.postValue(WorkState.MATCH)
            masterKey = key // master破棄
        } else {
            workState.postValue(WorkState.UN_MATCH)
        }
    }
}