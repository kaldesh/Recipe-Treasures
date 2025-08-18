package com.example.recipetreasures.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipetreasures.MealsAdapter
import com.example.recipetreasures.data.AllAPi
import com.example.recipetreasures.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mealsAdapter: MealsAdapter
    private lateinit var viewModel: MealsViewModel

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
        mealsAdapter = MealsAdapter(mutableListOf()) { meal ->
            Toast.makeText(requireContext(), "Clicked: ${meal.strMeal}", Toast.LENGTH_SHORT).show()
            // You can navigate to a details fragment here if needed
        }

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
            mealsAdapter.updateData(mealsList)
        }

        // ✅ Fetch meals on load
        viewModel.fetchAllMeals()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
