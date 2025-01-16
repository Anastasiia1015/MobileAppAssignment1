package com.example.myapplication2

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipesViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val allRecipes = listOf(
        Recipe(1, "Pizza", "Delicious cheesy pizza with toppings"),
        Recipe(2, "Burger", "Juicy burger with fresh veggies"),
        Recipe(3, "Pasta", "Creamy Alfredo pasta"),
        Recipe(4, "Noodles", "Spicy noodles with chicken"),
        Recipe(5, "Kebab", "Grilled kebabs with spices")
    )

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            if (query.length < 3) {
                _recipes.update { allRecipes }
            } else {
                val filteredRecipes = allRecipes.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                }
                _recipes.update { filteredRecipes }
            }
        }
    }
}
