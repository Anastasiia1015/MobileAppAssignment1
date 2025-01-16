package com.example.myapplication2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), RecipeAdapter.EventHandler {

    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupEdgeToEdgeInsets()
        setupRecyclerView()

        val searchView = findViewById<SearchView>(R.id.searchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchRecipes(newText)
                return true
            }
        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipes.collect { recipes ->
                    recipeAdapter.updateRecipes(recipes)
                }
            }
        }
    }

    private fun setupEdgeToEdgeInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        val recipeList = findViewById<RecyclerView>(R.id.recipes_list)
        recipeAdapter = RecipeAdapter(emptyList(), this) // Initially empty list
        recipeList.adapter = recipeAdapter
        recipeList.layoutManager = LinearLayoutManager(this)
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
        Toast.makeText(this, "Pressed recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    private fun likeRecipe(recipe: Recipe) {
        Toast.makeText(this, "Pressed like of recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    private fun shareRecipe(recipe: Recipe) {
        Toast.makeText(this, "Pressed share of recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }
}
