package com.example.recipetreasures

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import android.content.Context
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipetreasures.data.db.AppDatabase
import com.example.recipetreasures.data.repo.FavoredMealRepository
import com.example.recipetreasures.data.repo.MealsRepository
import com.example.recipetreasures.data.retrofit.AllAPi
import com.example.recipetreasures.databinding.FragmentFavouritesBinding
import com.example.recipetreasures.ui.FavoredMealViewModel
import com.example.recipetreasures.ui.FavoredMealViewModelFactory
import com.example.recipetreasures.ui.toUiModel
import kotlinx.coroutines.launch
import kotlin.getValue

class favouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MealsAdapter

    private val PREFS_NAME = "UserSession"
    private val KEY_EMAIL = "current_user_email"
    val prefs by lazy { requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    val currentUserEmail by lazy { prefs.getString(KEY_EMAIL, null) }

    private val dao by lazy { AppDatabase.getDatabase(requireContext().applicationContext).favoredMealDao() }
    private val favoredMealRepo by lazy { FavoredMealRepository(dao) }
    private val MealRepo by lazy { MealsRepository(AllAPi.recipeEndPoint) }
    private val favoredMealViewModel by viewModels<FavoredMealViewModel> { FavoredMealViewModelFactory(favoredMealRepo, MealRepo, currentUserEmail!!) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize adapter with a mutable list
        adapter = MealsAdapter(
            mutableListOf(),
            onItemClick = { meal ->
                val mealId = meal.id
                if (mealId.isNotBlank())
                    findNavController().navigate(
                        favouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(meal.id)
                    )
                else
                    Toast.makeText(requireContext(), "Meal ID is null", Toast.LENGTH_SHORT).show()
            },
            onFavoriteClick = { meal ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (favoredMealViewModel.isMealFavored(meal.id))
                        favoredMealViewModel.removeMealFromFavorites(meal.id)
                    else
                        favoredMealViewModel.addMealToFavorites(meal.id)
                }
            }
        )

        binding.recyclerViewFavorite.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorite.adapter = adapter


        // Observe results
        favoredMealViewModel.favoredMeals.observe(viewLifecycleOwner) { meals ->
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.updateData(meals.map { it.toUiModel(favoredMealViewModel.isMealFavored(it.idMeal!!)) })
            }
        }
        favoredMealViewModel.fetchFavoredMeals()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}