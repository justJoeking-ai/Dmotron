package com.justjoeking.dmotron

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DNDService {

    // http://dnd5eapi.co/api/
    // https://www.json.org

    // @todo: get this one working
    @GET("monsters/")
    fun listMonsters(): Call<List<Monster>>

    @GET("monsters/{monster}/")
    fun getMonster(@Path("monster") monster: Int): Call<Monster>
}
