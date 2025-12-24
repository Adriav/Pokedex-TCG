package com.adriav.tcgpokemon.navigation

/*
data object Home

data class Detail(val id: String)

@Composable
fun BasicNavigationWrapper() {
    val backStack = remember { mutableListOf<Any>(Home) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    HomeScreen()
                }
                is Detail -> NavEntry(key){

                }
                else -> NavEntry(key = Unit) {
                    Text(text = "ERROR!")
                }
            }
        }
    )
}
*/