package com.justjoeking.dmotron

import retrofit2.Retrofit

public class Retrofit {

    var retrofit = Retrofit.Builder()
        .baseUrl("http://dnd5eapi.co/api/")
        .build()

    var service = retrofit.create(DNDApiService::class.java)
}