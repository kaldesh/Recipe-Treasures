package com.example.recipetreasures

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipetreasures.data.db.AppDatabase
import com.example.recipetreasures.data.repo.FavoredMealRepository
import com.example.recipetreasures.data.retrofit.AllAPi
import com.example.recipetreasures.databinding.FragmentSearchBinding
import com.example.recipetreasures.data.repo.MealsRepository
import com.example.recipetreasures.ui.FavoredMealViewModel
import com.example.recipetreasures.ui.FavoredMealViewModelFactory
import com.example.recipetreasures.ui.HomeFragmentDirections
import com.example.recipetreasures.ui.SearchViewModel
import com.example.recipetreasures.ui.toUiModel
import kotlinx.coroutines.launch
import kotlin.getValue

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MealsAdapter
    private lateinit var viewModel: SearchViewModel

    private val PREFS_NAME = "UserSession"
    private val KEY_EMAIL = "current_user_email"
    val prefs by lazy { requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    val currentUserEmail by lazy { prefs.getString(KEY_EMAIL, null) }

    private val dao by lazy { AppDatabase.getDatabase(requireContext().applicationContext).favoredMealDao() }
    private val repo by lazy { FavoredMealRepository(dao) }
    private val FavoredMealViewModel by viewModels<FavoredMealViewModel> { FavoredMealViewModelFactory(dbRepo = repo,  userEmail = currentUserEmail!!) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
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
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(meal.id)
                    )
                else
                    Toast.makeText(requireContext(), "Meal ID is null", Toast.LENGTH_SHORT).show()
            },
            onFavoriteClick = { meal ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (FavoredMealViewModel.isMealFavored(meal.id))
                        FavoredMealViewModel.removeMealFromFavorites(meal.id)
                    else
                        FavoredMealViewModel.addMealToFavorites(meal.id)
                }
            }
        )

        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearch.adapter = adapter

        // ViewModel with factory (since it takes repository)
        val repository = MealsRepository(AllAPi.recipeEndPoint)
        viewModel = ViewModelProvider(this, SearchViewModelFactory(repository))
            .get(SearchViewModel::class.java)

        // Listen for text changes
        binding.searchInput.addTextChangedListener { text ->
            val query = text.toString()
            if (query.isNotBlank()) {
                viewModel.searchMeals(query)
            }
        }

        // Observe results
        viewModel.results.observe(viewLifecycleOwner) { meals ->
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.updateData(meals.map { it.toUiModel(FavoredMealViewModel.isMealFavored(it.idMeal!!)) })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


class SearchViewModelFactory(
    private val repository: MealsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

