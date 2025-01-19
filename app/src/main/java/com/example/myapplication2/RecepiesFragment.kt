package com.example.myapplication2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle

class RecipesFragment : Fragment(), RecipeAdapter.EventHandler {
    private val viewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeList = view.findViewById<RecyclerView>(R.id.recipes_list)
        val recipeAdapter = RecipeAdapter(emptyList(), this)
        val searchView = view.findViewById<SearchView>(R.id.searchView)

        recipeList.layoutManager = LinearLayoutManager(requireContext())
        recipeList.adapter = recipeAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length < 3) {
                    viewModel.setQuery("")
                } else {
                    viewModel.setQuery(newText)
                }
                return true
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recipesFlow.collect { recipes ->
                    recipeAdapter.updateRecipes(recipes)
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
}

class RecipesViewModel: ViewModel() {
    val recipes = listOf(
        Recipe(1, "Pizza", "Description of Pizza"),
        Recipe(2, "Burger", "Description of Burger"),
        Recipe(3, "Pasta", "Description of Pasta"),
        Recipe(4, "Noodles", "Description of Noodles"),
        Recipe(5, "Kebab", "Description of Kebab")
    )
    private val queryFlow = MutableStateFlow("")
    val recipesFlow = queryFlow
        .asStateFlow()
        .map {
        query -> recipes.filter { recipe -> recipe.title.contains(query,ignoreCase = true) }
    }
    fun setQuery(query: String){
        queryFlow.value = query
    }
}

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
    fun updateRecipes(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}

data class Recipe(val id: Int, val title: String, val description: String)