package com.example.recipetreasures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class OnboardingFragment : Fragment() {

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESC = "description"
        private const val ARG_IMAGE = "image"

        fun newInstance(title: String, description: String, imageRes: Int): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESC, description)
            args.putInt(ARG_IMAGE, imageRes)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)

        val titleText: TextView = view.findViewById(R.id.titleText)
        val descText: TextView = view.findViewById(R.id.descText)
        val imageView: ImageView = view.findViewById(R.id.imageView)

        arguments?.let {
            titleText.text = it.getString(ARG_TITLE)
            descText.text = it.getString(ARG_DESC)
            imageView.setImageResource(it.getInt(ARG_IMAGE))
        }

        return view
    }
}
