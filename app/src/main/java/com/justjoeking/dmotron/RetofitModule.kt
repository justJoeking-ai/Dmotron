package com.justjoeking.dmotron

import retrofit2.Retrofit

class Retrofit {

    var retrofit = Retrofit.Builder()
        .baseUrl("https://dnd5eapi.co/api/")
        .build()

    var service = retrofit.create(DNDService::class.java)
}