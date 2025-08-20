package com.example.recipetreasures

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val pages = listOf(
        OnboardingFragment.newInstance(
            "Welcome to Recipe Treasures",
            "Discover a world of delicious recipes from all cuisines, right at your fingertips!",
            R.drawable.img
        ),
        OnboardingFragment.newInstance(
            "Step-by-Step Guides",
            "Every recipe comes with detailed instructions, ingredients list, and helpful cooking tips.",
            R.drawable.food2
        ),
        OnboardingFragment.newInstance(
            "Cook with Videos",
            "Follow along with cooking videos and create amazing dishes with confidence!",
            R.drawable.food3
        )
    )


    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment = pages[position]
}