package com.asurspace.mynameischuck

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.asurspace.mynameischuck.contract.HasUuid
import com.asurspace.mynameischuck.contract.NumberListener
import com.asurspace.mynameischuck.contract.navigator
import com.asurspace.mynameischuck.databinding.FragmentChuckPhraseBinding
import com.github.javafaker.Faker
import kotlin.properties.Delegates
import kotlin.random.Random

class ChuckPhraseFragment : Fragment(), HasUuid, NumberListener {

    private var _binding: FragmentChuckPhraseBinding? = null
    private val binding get() = _binding!!

    private val faker = Faker()

    private var backgroundColor by Delegates.notNull<Int>()
    private lateinit var chuckFacts: String


    private val textColor: Int
        get() = if (Color.luminance(backgroundColor) > 0.3) {
            Color.BLACK
        } else {
            Color.WHITE
        }

    private var uuidArgument: String
        get() = requireArguments().getString(ARG_UUID)!!
        set(value) = requireArguments().putString(ARG_UUID, value)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getUuid()
        Log.d(TAG, "$uuidArgument: onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backgroundColor = savedInstanceState?.getInt(KEY_BACKGROUND_COLOR) ?: getRandomColor()
        chuckFacts = savedInstanceState?.getString(KEY_CHUCK_FACTS) ?: getChuckFacts()
        Log.d(TAG, "$uuidArgument: onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChuckPhraseBinding.inflate(inflater, container, false)

        setListeners()
        updateUi()

        Log.d(TAG, "$uuidArgument: onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "$uuidArgument: onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "$uuidArgument: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "$uuidArgument: onResume")
    }

    private fun getChuckFacts(): String {
        return faker.chuckNorris().fact()
    }

    private fun getRandomColor(): Int = -Random.nextInt(0xFFFFFF)

    private fun setListeners() {
        with(binding) {
            changeUuidTb.setOnClickListener {
                uuidArgument = navigator().generateUuid()
                navigator().update()
                updateUi()
            }

            changeBackgroundTb.setOnClickListener {
                backgroundColor = getRandomColor()
                updateUi()
            }

            nextFactTb.setOnClickListener {
                chuckFacts = getChuckFacts()
                updateUi()
            }

            launchNext.setOnClickListener {
                navigator().launchNext()
            }
        }
    }

    private fun updateUi() {
        with(binding) {
            uuidTv2.text = uuidArgument
            chuckFactsTv.text = chuckFacts
            root.setBackgroundColor(backgroundColor)
            uuidTv2.setTextColor(textColor)
            chuckFactsTv.setTextColor(textColor)
            numberTv.setTextColor(textColor)
        }
    }

    override fun onNewScreenNumber(number: Int) {
        binding.numberTv.text = getString(R.string.number, number)
    }

    override fun getUuid(): String {
        return uuidArgument
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "$uuidArgument: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "$uuidArgument: onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_BACKGROUND_COLOR, backgroundColor)
        outState.putString(KEY_CHUCK_FACTS, chuckFacts)
        Log.d(TAG, "$uuidArgument: onSaveInstance")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "$uuidArgument: onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "$uuidArgument: onDestroy")
    }

    companion object {

        @JvmStatic
        private val TAG = ChuckPhraseFragment::class.java.simpleName

        @JvmStatic
        private val ARG_UUID = "KEY_UUID"

        @JvmStatic
        private val KEY_BACKGROUND_COLOR = "KEY_BACKGROUND_COLOR"

        @JvmStatic
        private val KEY_CHUCK_FACTS = "KEY_CHUCK_PHRASE"

        @JvmStatic
        fun newInstance(uuid: String) =
            ChuckPhraseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_UUID, uuid)
                }
            }

    }

}