package com.justjoeking.dmotron

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.justjoeking.dmotron.model.Monster
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MonsterDetailActivity : AppCompatActivity() {
    companion object {
        // Nice to have the bundle keys as constants
        const val MONSTER_ID = "monster_id"
    }

    private lateinit var monsterDetail: Monster
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://www.dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_detail)
        setSupportActionBar(toolbar)

        val monsterIndex = intent.extras?.getString(MONSTER_ID) // eg. "Adult Black Dragon"

        if (monsterIndex.isNullOrEmpty()) {
            Log.e("Error", "Monster not found in bundle")
            finish()
        } else {
            fetchMonster(monsterIndex)
        }
    }

    private fun fetchMonster(monsterIndex: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.dnd5eapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DNDService::class.java).getMonster(monsterIndex)
            .enqueue(object : Callback<Monster> {
                override fun onFailure(
                    call: Call<Monster>?,
                    t: Throwable?
                ) {
                    Log.v("retrofit", "call failed")
                    Log.e("Call failure", "Aw shit", t)
                }

                override fun onResponse(
                    call: Call<Monster>?,
                    response: Response<Monster>?
                ) {
                    val monster: Monster? = response?.body()
                    monster?.let { updateViews(it) }
                }
            })
    }

    private fun updateViews(monster: Monster) {
        centertext.text =
            "\n\n" + monster.name +
                    "\n" + "Hit Points: " + monster?.hit_points.toString() +
                    "\n" + "AC: " + monster?.armor_class?.toLong() +
                    "\n" + " Size: " + monster?.size +
                    "\n" + " Type: " + monster?.type +
                    "\n" + " Challenge_rating: " + monster?.challenge_rating +
                    "\n" + " Subtype: " + monster?.subtype +
                    "\n" + " Alignment: " + monster?.alignment +
                    "\n" + " Armor_class: " + monster?.armor_class +
                    "\n" + " Hit_points: " + monster?.hit_points +
                    "\n" + " Hit_dice: " + monster?.hit_dice +
                    "\n" + " Strength: " + monster?.strength +
                    "\n" + " Dexterity: " + monster?.dexterity +
                    "\n" + " Constitution: " + monster?.constitution +
                    "\n" + " Intelligence: " + monster?.intelligence +
                    "\n" + " Wisdom: " + monster?.wisdom +
                    "\n" + " Charisma: " + monster?.charisma

//                              "\n"+ " id: " +  monster?.id
//                              "\n"+ " index: " +  monster?.index
//                    "\n" + " Speed:" +
//                    "\n" + "Walk: " + monster?.speed?.walk
//                                "\n" + "Fly: " + monster?.speed?.fly +
//                                "\n" + "Swim: " + monster?.speed?.swim +
//                                "\n" + "Burrow: " + monster?.speed?.burrow
//                    "\n" + " Wisdom: " + monster?.proficiencies
//                    "\n"+ " Charisma: " +  monster?.charisma + monster?.proficiencies?.name
//                    "\n"+ " Dexterity_save: " +  monster?.dexterity_save+
//                    "\n"+ " Constitution_save: " +  monster?.constitution_save+
//                    "\n"+ " Wisdom_save: " +  monster?.wisdom_save+
//                    "\n"+ " Charisma_save: " +  monster?.charisma_save+
//                    "\n"+ " Perception: " +  monster?.perception+
//                    "\n"+ " Stealth: " +  monster?.stealth+
//                    "\n"+ " Damage_vulnerabilities: " +  monster?.damage_vulnerabilities+
//                    "\n"+ " Damage_resistances: " +  monster?.damage_resistances+
//                    "\n"+ " Damage_immunities: " +  monster?.damage_immunities+
//                    "\n"+ " Condition_immunities: " +  monster?.condition_immunities
    }

    private fun setupMonsterDetail(monsterIndex: String) {
        val myArg = object : Callback<Monster> {
            override fun onFailure(call: Call<Monster>?, t: Throwable?) {
                Log.v("retrofit", "call failed")
            }

            override fun onResponse(
                call: Call<Monster>?,
                response: Response<Monster>?
            ) {
                Log.d("AllMonsters", response.toString())
                monsterDetail = response?.body() ?: Monster(name = "", url = "")
            }
        }
        retrofit.create(DNDService::class.java).getMonster(monsterIndex = monsterIndex)
            .enqueue(myArg)
    }
}
