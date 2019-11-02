package com.justjoeking.dmotron

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {
    @GET("pokemon/")
    fun listPokemon(): Call<List<Pokemon>>
}