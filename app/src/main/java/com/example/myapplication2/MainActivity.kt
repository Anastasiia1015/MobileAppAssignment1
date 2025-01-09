package com.example.myapplication2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recipeList = findViewById<RecyclerView>(R.id.recipes_list)
        val recipes = listOf(
            Recipe(1, "Pizza", "Description of Pizza"),
            Recipe(2, "Burger", "Description of Burger"),
            Recipe(3, "Pasta", "Description of Pasta"),
            Recipe(4, "Noodles", "Description of Noodles"),
            Recipe(5, "Kebab", "Description of Kebab")
        )
        recipeList.adapter = RecipeAdapter(recipes, this)
        recipeList.layoutManager = LinearLayoutManager(this)
    }

    override fun onRecipeClicked(recipe: Recipe) {
        Toast.makeText(this, "Pressed recipe with id:${recipe.id}", Toast.LENGTH_LONG).show()
    }

    override fun onLikeClicked(recipe: Recipe) {
        Toast.makeText(this, "Pressed like of recipe with id:${recipe.id}", Toast.LENGTH_LONG)
            .show()
    }

    override fun onShareClicked(recipe: Recipe) {
        Toast.makeText(this, "Pressed share of recipe with id:${recipe.id}", Toast.LENGTH_LONG)
            .show()
    }
}

class RecipeAdapter(private val recipes: List<Recipe>, private val eventHandler: EventHandler) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    interface EventHandler {
        fun onRecipeClicked(recipe: Recipe)
        fun onLikeClicked(recipe: Recipe)
        fun onShareClicked(recipe: Recipe)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.recipe_title)
        private val description: TextView = view.findViewById(R.id.recipe_description)
        val shareButton: ImageButton = view.findViewById(R.id.share)
        val likeButton: ImageButton = view.findViewById(R.id.like)
        fun setRecipe(recipe: Recipe) {
            title.text = recipe.title
            description.text = recipe.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recipes.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRecipe(recipes[position])
        holder.itemView.setOnClickListener {
            eventHandler.onRecipeClicked(recipes[position])
        }
        holder.shareButton.setOnClickListener {
            eventHandler.onShareClicked(recipes[position])
        }
        holder.likeButton.setOnClickListener {
            eventHandler.onLikeClicked(recipes[position])
        }
    }
}

data class Recipe(val id: Int, val title: String, val description: String)