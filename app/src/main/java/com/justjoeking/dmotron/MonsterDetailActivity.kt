package com.justjoeking.dmotron

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justjoeking.dmotron.model.Monster
import com.justjoeking.dmotron.network.HttpClient
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_monster_detail.*
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
                    Timber.v("fetchMonster call failed")
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
        // Share Button
        share_fab.setOnClickListener {
            MonsterUtils().shareMonster(monster, this)
        }

        // Name (toolbar)
        title = monster.name

        // Name (main view)
        monster_name.text = monster.name

        // Medium Humanoid (Human), lawful neutral
        var monsterDetails = ""
        monsterDetails += monster.size + " "
        monsterDetails += monster.type
        monster.subtype?.let {
            if (monster.subtype.isNotEmpty()) {
                monsterDetails += " (${monster.subtype})"
            }
        }
        monsterDetails += ", ${monster.alignment}"
        monster_type_alignment.text = "(" + monsterDetails + ") "
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
        monsterDetailsText += "\nHit Points: " + monster.hit_points.toString()
        monsterDetailsText += "\nAC: " + monster.armor_class.toLong()
        monsterDetailsText += "\nSize: " + monster.size
        monsterDetailsText += "\nType: " + monster.type
        monsterDetailsText += "\nCR: " + monster.challenge_rating
        monsterDetailsText += "\nSubtype: " + monster.subtype
        monsterDetailsText += "\nAlignment: " + monster.alignment
        monsterDetailsText += "\nHP: " + monster.hit_points
        monsterDetailsText += "\nHit Dice: " + monster.hit_dice
        monsterDetailsText += "\nStrength: " + monster.strength.toLong()
        monsterDetailsText += "\nDexterity: " + monster.dexterity.toLong()
        monsterDetailsText += "\nConstitution: " + monster.constitution.toLong()
        monsterDetailsText += "\nIntelligence: " + monster.intelligence.toLong()
        monsterDetailsText += "\nWisdom: " + monster.wisdom.toLong()
        monsterDetailsText += "\nCharisma: " + monster.charisma.toLong()
        if (monster.speed != null) {
            monsterDetailsText += "\nSpeed: "
            if (monster.speed.walk.isNotEmpty()) {
                monsterDetailsText += "\nWalk: " + monster.speed.walk
            }

            if (monster.speed.fly.isNotEmpty()) {
                monsterDetailsText += "\nFly: " + monster.speed.fly
            }

            if (monster.speed.swim.isNotEmpty()) {
                monsterDetailsText += "\nSwim: " + monster.speed.swim
            }

            if (monster.speed.burrow.isNotEmpty()) {
                monsterDetailsText += "\nBurrow: " + monster.speed.burrow
            }

            if (monster.speed.climb.isNotEmpty()) {
                monsterDetailsText += "\nClimb: " + monster.speed.climb
            }

            if (monster.speed.hover.isNotEmpty()) {
                monsterDetailsText += "\nHover: " + monster.speed.hover
            }
        }

        if (monster.proficiencies.isNotEmpty()) {
            monsterDetailsText += "\nProficiencies: "
            for (prof in monster.proficiencies) {
                monsterDetailsText += "\n" + prof.name
            }
        }
//        newText += "\n Dexterity_save: " + monster.dexterity_save +
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
            monsterDetailsText += "\n index: " + monster.index
        }
    }
}
