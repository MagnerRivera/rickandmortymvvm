package com.example

/**
 * estructura de caracteres
 */
data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: Location,
    val location: Location,
    val image: String,
    val episodesIds: List<Int>,
)

/**
 * estructura de localización
 */
data class Location(
    val id: Int,
    val name: String,
)

/**
 * estructura de episodios
 */
data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val charactersIds: List<Int>,
)