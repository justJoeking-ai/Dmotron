package com.justjoeking.dmotron

import com.justjoeking.dmotron.model.Monster
import com.justjoeking.dmotron.model.Spell
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DNDService {

    // TODO: build repository layer with caching

    @GET("monsters/")
    fun listMonsters(): Call<MonsterResponse>

    @GET("monsters/{monsterIndex}/")
    fun getMonster(@Path("monsterIndex") monsterIndex: String): Call<Monster>

    @GET("monsters/{monsterName}/")
    fun getMonsterByName(@Path("monsterName") monsterName: String): Call<Monster>

    @GET("spells/")
    fun listSpell(): Call<SpellResponse>

    @GET("spells/{spell}/")
    fun getSpell(@Path("spell") spell: String): Call<Spell>
}
