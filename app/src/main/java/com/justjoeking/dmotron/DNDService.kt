package com.justjoeking.dmotron

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DNDService {

    //http://dnd5eapi.co/api/

    @GET("monsters/{monster}/")
    fun listMonsters(@Path("monster") monster: String): Call<List<Monster>>
}
