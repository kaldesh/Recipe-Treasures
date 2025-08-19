package com.example.recipetreasures.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipetreasures.MealsAdapter
import com.example.recipetreasures.data.db.AppDatabase
import com.example.recipetreasures.data.repo.FavoredMealRepository
import com.example.recipetreasures.data.repo.MealsRepository
import com.example.recipetreasures.data.retrofit.AllAPi
import com.example.recipetreasures.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import kotlin.getValue

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealsAdapter: MealsAdapter
    private lateinit var viewModel: MealsViewModel

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Setup Adapter
        mealsAdapter = MealsAdapter(
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


        // ✅ Setup RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mealsAdapter
        }

        // ✅ ViewModel & Repository
        val repository = MealsRepository(AllAPi.recipeEndPoint)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MealsViewModel(repository) as T
            }
        })[MealsViewModel::class.java]

        // ✅ Observe LiveData
        viewModel.meals.observe(viewLifecycleOwner) { mealsList ->
            viewLifecycleOwner.lifecycleScope.launch {
                mealsAdapter.updateData(mealsList.map { it.toUiModel(FavoredMealViewModel.isMealFavored(it.idMeal!!)) })
            }
        }

        // ✅ Fetch meals on load
        viewModel.fetchAllMeals()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
