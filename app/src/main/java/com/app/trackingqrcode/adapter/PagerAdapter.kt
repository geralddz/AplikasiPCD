package com.app.trackingqrcode.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.trackingqrcode.fragment.FragmentShift1
import com.app.trackingqrcode.fragment.FragmentShift3

class PagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FragmentShift1()
            1 -> FragmentShift3()
            else -> FragmentShift1()
        }
    }
}