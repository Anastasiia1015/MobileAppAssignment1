package com.example.myapplication2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.SearchView
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
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.debounce
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers


class RecipesFragment : Fragment(), RecipeAdapter.EventHandler {

    private val viewModel: RecipesViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter  // Declare at the class level

    private val recipesRecyclerView
        get() = requireView().findViewById<RecyclerView>(R.id.recyclerView)
    private val searchView
        get() = requireView().findViewById<SearchView>(R.id.search_view)

    // Override onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recipes, container, false)
    }

    // Override onCreate to retrieve arguments
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


class RecipesViewModel : ViewModel() {

    private val allRecipes = listOf(
        Recipe(1, "Pizza", "Description of Pizza"),
        Recipe(2, "Burger", "Description of Burger"),
        Recipe(3, "Pasta", "Description of Pasta"),
        Recipe(4, "Noodles", "Description of Noodles"),
        Recipe(5, "Kebab", "Description of Kebab")
    )
    private val _recipes = MutableStateFlow<List<Recipe>>(allRecipes)
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    private val _uiState = MutableStateFlow<RecipesUiState>(RecipesUiState.Loading)
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(300)
                .collect { query ->
                    _uiState.value = RecipesUiState.Loading
                    delay(1000)
                    val filteredRecipes = if (query.length < 3) {
                        allRecipes  // Use `allRecipes` directly
                    } else {
                        allRecipes.filter {
                            it.title.contains(query, ignoreCase = true) ||
                                    it.description.contains(query, ignoreCase = true)
                        }
                    }

                    _recipes.value = filteredRecipes
                    if (filteredRecipes.isEmpty()) {
                        _uiState.value = RecipesUiState.Error("No recipes found.")
                    } else {
                        _uiState.value = RecipesUiState.Success(filteredRecipes)
                    }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }
}

sealed class RecipesUiState {
    object Loading : RecipesUiState()
    data class Success(val recipes: List<Recipe>) : RecipesUiState()
    data class Error(val message: String) : RecipesUiState()
}

class RecipeAdapter(private var recipes: List<Recipe>, private val eventHandler: EventHandler) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {
    interface EventHandler {
        fun onRecipeClicked(recipe: Recipe)
        fun onLikeClicked(recipe: Recipe)
        fun onShareClicked(recipe: Recipe)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.recipeTitle)
        private val description: TextView = view.findViewById(R.id.recipeDescription)
        val shareButton: ImageButton = view.findViewById(R.id.recipeShare)
        val likeButton: ImageButton = view.findViewById(R.id.recipeLike)
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