package com.fynzero.timemanagement.view.fragment.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup viewpager
        val viewPagerAdapter = ViewPagerAdapter(requireActivity(), childFragmentManager)
        viewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }
}