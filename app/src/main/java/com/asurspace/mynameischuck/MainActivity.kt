package com.asurspace.mynameischuck

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.asurspace.mynameischuck.contract.HasUuid
import com.asurspace.mynameischuck.contract.Navigator
import com.asurspace.mynameischuck.contract.NumberListener
import com.asurspace.mynameischuck.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private val fragmentListener: FragmentManager.FragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            update()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_container, ChuckPhraseFragment.newInstance(generateUuid()))
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun generateUuid() =
        UUID.randomUUID().toString()

    override fun update() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_container)

        if (currentFragment is HasUuid) {
            binding.uuidTv1.text = currentFragment.getUuid()
        } else {
            binding.uuidTv1.text = ""
        }

        if (currentFragment is NumberListener) {
            currentFragment.onNewScreenNumber(supportFragmentManager.backStackEntryCount + 1)
        }
    }

    override fun launchNext() {
        openFragment(ChuckPhraseFragment.newInstance(generateUuid()))
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container, fragment)
            .commit()
    }

}