package com.justjoeking.dmotron

import android.os.Bundle
import android.os.Message
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_monster_card.*
import kotlinx.android.synthetic.main.monster_layout.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MonsterCardActivity : AppCompatActivity() {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://www.dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
val i = "hey"




}
