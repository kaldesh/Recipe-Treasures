package com.example.recipetreasures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipetreasures.data.retrofit.AllAPi
import com.example.recipetreasures.databinding.FragmentSearchBinding
import com.example.recipetreasures.data.repo.MealsRepository
import com.example.recipetreasures.ui.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MealsAdapter
    private lateinit var viewModel: SearchViewModel

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
        adapter = MealsAdapter(mutableListOf()) { meal ->
            val mealId = meal.idMeal
            if (mealId != null)
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(meal.idMeal!!))
            else
                Toast.makeText(requireContext(), "Meal ID is null", Toast.LENGTH_SHORT).show()
        }

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
            adapter.updateData(meals)
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

