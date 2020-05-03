package com.justjoeking.dmotron

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justjoeking.dmotron.model.Monster
import com.justjoeking.dmotron.network.HttpClient
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_monster_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class MonsterDetailActivity : AppCompatActivity() {
    companion object {
        // Nice to have the bundle keys as constants
        const val MONSTER_ID = "monster_id"
    }

    private lateinit var monsterDetail: Monster
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://www.dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(HttpClient.client)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_detail)
        setSupportActionBar(toolbar)

        val monsterIndex = intent.extras?.getString(MONSTER_ID) // eg. "Adult Black Dragon"

        if (monsterIndex.isNullOrEmpty()) {
            Timber.e("Monster not found in bundle")
            fetchMonster("orc")
        } else {
            fetchMonster(monsterIndex)
        }
    }

    private fun fetchMonster(monsterIndex: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.dnd5eapi.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(HttpClient.client)
            .build()

        retrofit.create(DNDService::class.java).getMonster(monsterIndex)
            .enqueue(object : Callback<Monster> {
                override fun onFailure(
                    call: Call<Monster>?,
                    t: Throwable?
                ) {
                    Timber.v("retrofit call failed")
                    Timber.e(t)
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

        // Name (toolbar)
        title = monster.name

        // Name (main view)
        monster_name.text = monster.name

        // Medium Humanoid (Human), lawful neutral
        var monsterDetails = ""
        monsterDetails += monster.size + " "
        monsterDetails += monster.type
        if (!monster.subtype.isNullOrEmpty()) {
            monsterDetails += " (${monster.subtype})"
        }

        monsterDetails += ", ${monster.alignment}"
        monster_detail.text = "($monsterDetails) "
        monster_ac.text = monster.armor_class.toString()

        // =======
        // Armor Class 10
        // Hit Points 10
        // Speed 30 ft., sly 120ft.
        // =======
        // Stat block SDCIWC
        // =======
        // Saving Throws Dex +3, Con +4, Cha +6
        // Damage Resistances cold; bludgeoning
        // Damage Immunities
        // Condition Immunities
        // Senses
        // Languages
        // Challenge
        // =======
        // Special Thingies
        // Actions


        var monsterDetailsText = "\n\n" + monster.name
        monsterDetailsText += "\n" + "Hit Points: " + monster.hit_points.toString()
        monsterDetailsText += "\n" + "AC: " + monster.armor_class.toLong()
        monsterDetailsText += "\n" + "Size: " + monster.size
        monsterDetailsText += "\n" + "Type: " + monster.type
        monsterDetailsText += "\n" + "CR: " + monster.challenge_rating
        monsterDetailsText += "\n" + "Subtype: " + monster.subtype
        monsterDetailsText += "\n" + "Alignment: " + monster.alignment
        monsterDetailsText += "\n" + "HP: " + monster.hit_points
        monsterDetailsText += "\n" + "Hit Dice: " + monster.hit_dice
        monsterDetailsText += "\n" + "Strength: " + monster.strength.toLong()
        monsterDetailsText += "\n" + "Dexterity: " + monster.dexterity.toLong()
        monsterDetailsText += "\n" + "Constitution: " + monster.constitution.toLong()
        monsterDetailsText += "\n" + "Intelligence: " + monster.intelligence.toLong()
        monsterDetailsText += "\n" + "Wisdom: " + monster.wisdom.toLong()
        monsterDetailsText += "\n" + "Charisma: " + monster.charisma.toLong()
        if (monster.speed != null) {
            monsterDetailsText += "\n" + "Speed: "
            if (monster.speed.walk.isNotEmpty()) {
                monsterDetailsText += "\n" + "Walk: " + monster.speed.walk
            }

            if (monster.speed.fly.isNotEmpty()) {
                monsterDetailsText += "\n" + "Fly: " + monster.speed.fly
            }

            if (monster.speed.swim.isNotEmpty()) {
                monsterDetailsText += "\n" + "Swim: " + monster.speed.swim
            }

            if (monster.speed.burrow.isNotEmpty()) {
                monsterDetailsText += "\n" + "Burrow: " + monster.speed.burrow
            }
        }

        if (monster.proficiencies.isNotEmpty()) {
            monsterDetailsText += "\n" + "Proficiencies: "
            for (prof in monster.proficiencies) {
                monsterDetailsText += "\n" + prof.name
            }
        }
//        newText += "\n" + " Dexterity_save: " + monster.dexterity_save +
//                    "\n"+ " Constitution_save: " +  monster.constitution_save+
//                    "\n"+ " Wisdom_save: " +  monster.wisdom_save+
//                    "\n"+ " Charisma_save: " +  monster.charisma_save+

//                    "\n"+ " Perception: " +  monster.perception+
//                    "\n"+ " Stealth: " +  monster.stealth+
//                    "\n"+ " Damage_vulnerabilities: " +  monster.damage_vulnerabilities+
//                    "\n"+ " Damage_resistances: " +  monster.damage_resistances+
//                    "\n"+ " Damage_immunities: " +  monster.damage_immunities+
//                    "\n"+ " Condition_immunities: " +  monster.condition_immunities

        // Only show this on debug builds
        if (BuildConfig.DEBUG) {
            monsterDetailsText += "\n" + " index: " + monster.index
        }

//        monster_name.text = monsterDetailsText
    }

    private fun setupMonsterDetail(monsterIndex: String) {
        val myArg = object : Callback<Monster> {
            override fun onFailure(call: Call<Monster>?, t: Throwable?) {
                Timber.v("call failed")
            }

            override fun onResponse(
                call: Call<Monster>?,
                response: Response<Monster>?
            ) {
                Timber.d("AllMonsters:" + response.toString())
                monsterDetail = response?.body() ?: Monster(name = "", url = "")
            }
        }
        retrofit.create(DNDService::class.java).getMonster(monsterIndex = monsterIndex)
            .enqueue(myArg)
    }
}
