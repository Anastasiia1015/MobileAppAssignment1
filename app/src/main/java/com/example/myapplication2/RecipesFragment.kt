package com.example.myapplication2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class RecipesFragment : Fragment(), RecipeAdapter.EventHandler {
    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        setupSearchView(view)
        observeRecipes()
    }

    private fun setupRecyclerView(view: View) {
        val recipeList = view.findViewById<RecyclerView>(R.id.recipes_list)
        recipeAdapter = RecipeAdapter(emptyList(), this)
        recipeList.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchView(view: View) {
        view.findViewById<SearchView>(R.id.searchView)?.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String): Boolean {
                    viewModel.searchRecipes(newText)
                    return true
                }
            })
        }
    }

    private fun observeRecipes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipes.collect { recipes ->
                    recipeAdapter.updateRecipes(recipes)
                }
            }
        }
    }

    override fun onRecipeClicked(recipe: Recipe) {
        showRecipeDetails(recipe)
    }

    override fun onLikeClicked(recipe: Recipe) {
        likeRecipe(recipe)
    }

    override fun onShareClicked(recipe: Recipe) {
        shareRecipe(recipe)
    }

    private fun showRecipeDetails(recipe: Recipe) {
        Toast.makeText(requireContext(), "Pressed recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    private fun likeRecipe(recipe: Recipe) {
        Toast.makeText(requireContext(), "Pressed like of recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    private fun shareRecipe(recipe: Recipe) {
        Toast.makeText(requireContext(), "Pressed share of recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance() = RecipesFragment()
    }
}