package com.fynzero.timemanagement.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.view.fragment.stats.AllFragment
import com.fynzero.timemanagement.view.fragment.stats.DailyFragment

class ViewPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DailyFragment()
            1 -> fragment = AllFragment()
        }
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.resources.getString(R.string.daily)
            else -> context.resources.getString(R.string.all)
        }
    }

    override fun getCount(): Int = 2
}