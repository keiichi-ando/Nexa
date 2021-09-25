package com.ex.nexa.ui.main

import android.content.Context
import android.media.SoundPool
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.ex.nexa.MainActivity
import com.ex.nexa.R
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.properties.Delegates

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainActivity: MainActivity
        get() = (activity as MainActivity)
    private lateinit var viewModel: MainViewModel
    private val soundPool = SoundPool.Builder().setMaxStreams(2).build()
    private var soundAlert by Delegates.notNull<Int>()
    private lateinit var textView: TextView
    private lateinit var editTextMaster: EditText
    private lateinit var editText: EditText
    private lateinit var imageMatch: ImageView
    private lateinit var imageUnMatch: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundAlert = soundPool.load(context, R.raw.se_alert, 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setBackKeyEvent()
        observeViewModel()

        textView = text_state
        editTextMaster = input_master
        editText = input_target
        imageMatch = image_match
        imageUnMatch = image_un_match
        setEditEventListener()
    }

    private fun setBackKeyEvent() {
        mainActivity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (viewModel.getWorkState().value != MainViewModel.WorkState.EMPTY) {
                        viewModel.initWorks()
                    } else {
                        if (isEnabled) {
                            isEnabled = false
                            requireActivity().onBackPressed()
                        }
                    }
                }
            })
    }

    private fun setEditEventListener() {
        editTextMaster.apply {
            setOnKeyListener { _, keyCode, keyEvent ->
                if (keyEvent.action != KeyEvent.ACTION_UP || keyCode != KeyEvent.KEYCODE_ENTER) {
                    return@setOnKeyListener false
                }
                hideSoftKey(editTextMaster)
                viewModel.onEditCommit((editTextMaster.text ?: "").toString())
                return@setOnKeyListener true
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hideSoftKey(editTextMaster)
                }
            }
        }

        editText.apply {
            setOnKeyListener { _, keyCode, keyEvent ->
                if (keyEvent.action != KeyEvent.ACTION_UP || keyCode != KeyEvent.KEYCODE_ENTER) {
                    return@setOnKeyListener false
                }
                hideSoftKey(editText)
                viewModel.onEditCommit((editText.text ?: "").toString())
                return@setOnKeyListener true
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    hideSoftKey(editText)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getWorkState().observe(viewLifecycleOwner, {
            when (it) {
                MainViewModel.WorkState.EMPTY -> onReady()
                MainViewModel.WorkState.COMPARE -> onCompare()
                MainViewModel.WorkState.MATCH -> onMatch()
                MainViewModel.WorkState.UN_MATCH -> onUnMatchAlert()
            }
        })
    }

    private fun onReady() {
        textView.text = getString(R.string.require_input_master)
        editTextMaster.editableText.clear()
        editText.editableText.clear()
        editTextMaster.isEnabled = true
        editTextMaster.requestFocus()
    }

    private fun onCompare() {
        imageMatch.visibility = ImageView.INVISIBLE
        imageUnMatch.visibility = ImageView.INVISIBLE

        textView.text = getString(R.string.require_input_target)
        editTextMaster.isEnabled = false
        editText.requestFocus()
    }

    private fun onMatch() {
        imageMatch.visibility = ImageView.VISIBLE
        imageUnMatch.visibility = ImageView.INVISIBLE
        onReady()
    }

    private fun onUnMatchAlert() {
        imageMatch.visibility = ImageView.INVISIBLE
        imageUnMatch.visibility = ImageView.VISIBLE

        textView.text = getString(R.string.require_input_retry)
        editText.editableText.clear()
        editText.requestFocus()
        soundPool.play(soundAlert, 1.0f, 1.0f, 0, 0, 1.0f)
    }

    private fun hideSoftKey(view: View) {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}