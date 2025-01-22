package com.example.myapplication2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.google.android.material.progressindicator.CircularProgressIndicator
import android.widget.Button
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class RecipesFragment : Fragment(), RecipeAdapter.EventHandler {
    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    private val recipesRecyclerView
        get() = requireView().findViewById<RecyclerView>(R.id.recipes_list)
    private val searchView
        get() = requireView().findViewById<SearchView>(R.id.searchView)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {

        }
        recipeAdapter = RecipeAdapter(emptyList(), this)
        recipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        recipesRecyclerView.adapter = recipeAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.updateSearchQuery(newText)
                return true
            }
        })

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    withContext(Dispatchers.Main) {
                        updateUIState(uiState)
                    }
                }
            }
        }
    }

    override fun onRecipeClicked(recipe: Recipe) {
        Toast.makeText(requireContext(), "Pressed recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    override fun onLikeClicked(recipe: Recipe) {
        Toast.makeText(requireContext(), "Pressed like of recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    override fun onShareClicked(recipe: Recipe) {
        Toast.makeText(requireContext(), "Pressed share of recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    private fun updateUIState(state: RecipesUiState) {
        view?.let { view ->
            val progressIndicator: CircularProgressIndicator = view.findViewById(R.id.circularProgressIndicator)
            when (state) {
                is RecipesUiState.Loading -> {
                    progressIndicator.visibility = View.VISIBLE
                }

                is RecipesUiState.Success -> {
                    progressIndicator.visibility = View.GONE
                    recipeAdapter.updateRecipes(state.recipes)
                }

                is RecipesUiState.Error -> {
                    progressIndicator.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

sealed class RecipesUiState {
    object Loading : RecipesUiState()
    data class Success(val recipes: List<Recipe>) : RecipesUiState()
    data class Error(val message: String) : RecipesUiState()
}
