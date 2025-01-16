package com.example.myapplication2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecipeAdapter(private var recipes: List<Recipe>, private val eventHandler: EventHandler) :
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

    override fun getItemCount(): Int = recipes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setRecipe(recipes[position])
        holder.itemView.setOnClickListener { eventHandler.onRecipeClicked(recipes[position]) }
        holder.shareButton.setOnClickListener { eventHandler.onShareClicked(recipes[position]) }
        holder.likeButton.setOnClickListener { eventHandler.onLikeClicked(recipes[position]) }
    }

    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
