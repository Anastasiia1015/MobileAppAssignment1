package com.example.myapplication2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), RecipeAdapter.EventHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupEdgeToEdgeInsets()
        setupRecyclerView()
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
        val recipes = getRecipes()
        recipeList.adapter = RecipeAdapter(recipes, this)
        recipeList.layoutManager = LinearLayoutManager(this)
    }

    private fun getRecipes(): List<Recipe> {
        return listOf(
            Recipe(1, "Pizza", "Description of Pizza"),
            Recipe(2, "Burger", "Description of Burger"),
            Recipe(3, "Pasta", "Description of Pasta"),
            Recipe(4, "Noodles", "Description of Noodles"),
            Recipe(5, "Kebab", "Description of Kebab")
        )
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