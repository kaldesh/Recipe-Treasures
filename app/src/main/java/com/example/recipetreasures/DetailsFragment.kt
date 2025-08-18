package com.example.recipetreasures

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.recipetreasures.data.AllAPi
import com.example.recipetreasures.databinding.FragmentDetailsBinding
import com.example.recipetreasures.ui.MealDetailsViewModel
import com.example.recipetreasures.ui.MealDetailsViewModelFactory
import com.example.recipetreasures.ui.MealsRepository
import com.example.recipetreasures.ui.MealsUiModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants

class DetailsFragment : Fragment() {

    private val repo = MealsRepository(AllAPi.recipeEndPoint)
    private val viewModel by viewModels<MealDetailsViewModel> { MealDetailsViewModelFactory(repo) }

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    val args: DetailsFragmentArgs by navArgs()

    private var currentTime = 0f
    private var playbackState: PlayerConstants.PlayerState = PlayerConstants.PlayerState.UNSTARTED

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.ytPlayerView.release() // Release the player
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            currentTime = savedInstanceState.getFloat("video_current_time", 0f)
            // Restore playbackState as PlayerConstants.PlayerState
            val restoredStateOrdinal = savedInstanceState.getInt("playback_state_ordinal", PlayerConstants.PlayerState.UNSTARTED.ordinal)
            playbackState = PlayerConstants.PlayerState.values()[restoredStateOrdinal]
        }

        viewModel.getMealById(args.recipeId)
        viewModel.mealDetails.observe(viewLifecycleOwner) { meal ->
            meal?.let { setupUI(it) } // Check for null if meal can be null
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("video_current_time", currentTime)
        // Save playbackState ordinal
        outState.putInt("playback_state_ordinal", playbackState.ordinal)
    }

    private fun setupUI(meal: MealsUiModel) {
        val radiusPx = (24 * resources.displayMetrics.density).toInt()
        binding.nameTv.text = meal.name
        Glide.with(this)
            .load(meal.thumbnailUrl)
            .transform(FitCenter(), RoundedCorners(radiusPx))
            .into(binding.imageView)

        binding.catagoryTv.text = meal.category
        binding.areaTv.text = meal.area
        binding.instructionsTv.text = meal.instructions
        binding.ingredientsTv.text = meal.ingredients.joinToString("\n")
        binding.measuresTv.text = meal.measures.joinToString("\n")

        val youTubePlayerView = binding.ytPlayerView
        lifecycle.addObserver(youTubePlayerView) // Add YouTubePlayerView to lifecycle observers


        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = extractYoutubeId(meal.youtubeUrl)

                // Use loadVideo to automatically resume playback if it was previously playing
                if (playbackState == PlayerConstants.PlayerState.PLAYING || playbackState == PlayerConstants.PlayerState.BUFFERING) {
                    youTubePlayer.loadVideo(videoId, currentTime)
                } else {
                    // Otherwise, use cueVideo to just load the thumbnail and wait for the user to press play
                    youTubePlayer.cueVideo(videoId, currentTime)
                }
            }            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                currentTime = second
            }
            override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                playbackState = state
            }

        })    }

    private fun extractYoutubeId(url: String): String {
        val regex = "(?<=watch\\?v=|/videos/|embed/|youtu.be/)[^#&?\\n]*".toRegex()
        return regex.find(url)?.value ?: ""
    }
}
    