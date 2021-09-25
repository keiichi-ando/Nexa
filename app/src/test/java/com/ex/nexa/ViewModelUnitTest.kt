package com.ex.nexa

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ex.nexa.ui.main.MainViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ViewModelUnitTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun vm_ready() {
        val viewModel = MainViewModel()
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.EMPTY)
    }

    @Test
    fun vm_compare() {
        val viewModel = MainViewModel()
        viewModel.onEditCommit("test123")
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.COMPARE)
    }

    @Test
    fun vm_compare_match() {
        val viewModel = MainViewModel()
        viewModel.onEditCommit("test123")
        viewModel.onEditCommit("test123")
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.MATCH)
    }

    @Test
    fun vm_compare_un_match() {
        val viewModel = MainViewModel()
        viewModel.onEditCommit("test123")
        viewModel.onEditCommit("test456")
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.UN_MATCH)
    }

    @Test
    fun vm_compare_un_match_retry() {
        val viewModel = MainViewModel()
        viewModel.onEditCommit("test123")
        viewModel.onEditCommit("test456")
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.UN_MATCH)
        viewModel.onEditCommit("test123")
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.MATCH)

    }

    @Test
    fun vm_compare_match_2nd_cycle() {
        val viewModel = MainViewModel()
        viewModel.onEditCommit("test123") // master
        viewModel.onEditCommit("test123")
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.MATCH)
        viewModel.onEditCommit("test123") // master
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.COMPARE)
        viewModel.onEditCommit("test123")
        assertEquals(viewModel.getWorkState().value, MainViewModel.WorkState.MATCH)

    }

}