package com.justjoeking.dmotron

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DNDService {
    
    @GET("monsters/")
    fun listMonsters(): Call<MonsterResponse>

    @GET("monsters/{monster}/")
    fun getMonster(@Path("monster") monster: Int): Call<Monster>

    @GET("spells/")
    fun listSpell(): Call<SpellResponse>

    @GET("spells/{spell}/")
    fun getSpell(@Path("spell") spell: Int): Call<Spell>
}
