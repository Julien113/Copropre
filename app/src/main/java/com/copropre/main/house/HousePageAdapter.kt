package com.copropre.main.house

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.copropre.common.models.House
class HousePageAdapter(frag: Fragment, private val house: House) : FragmentStateAdapter(frag) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 ->
                HouseTasksFragment(house)
            1 ->
                HouseHistoryFragment(house)
            else -> HouseBalanceFragment(house)
        }
    }
}