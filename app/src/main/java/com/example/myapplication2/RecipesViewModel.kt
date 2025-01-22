package com.example.myapplication2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {
    private val allRecipes = listOf(
        Recipe(1, "Pizza", "Delicious cheesy pizza with toppings"),
        Recipe(2, "Burger", "Juicy burger with fresh veggies"),
        Recipe(3, "Pasta", "Creamy Alfredo pasta"),
        Recipe(4, "Noodles", "Spicy noodles with chicken"),
        Recipe(5, "Kebab", "Grilled kebabs with spices")
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
