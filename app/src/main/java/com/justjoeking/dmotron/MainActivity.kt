package com.justjoeking.dmotron

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import kotlin.collections.ArrayList
import kotlin.math.ceil
import retrofit2.Retrofit
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainActivity : AppCompatActivity() {

    // remove as read:
    // https://www.reddit.com/r/StrangePlanet/
    // https://fragmentedpodcast.com/
    // https://www.stilldrinking.org/programming-sucks

    var rollhistory = ArrayList<Int>(0)

//    https://play.google.com/apps/publish/?account=9031693838262703476#AdminPlace


    val retrofit = Retrofit.Builder()
        .baseUrl("http://dnd5eapi.co/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    enum class status {
        // just for science
        EASY,
        NORMAL,
        HARD
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        main_fab.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    this,
                    R.color.Btncolor
                )
            )
        )

        setupMonsterList()
        setupRightFabClick()
    }

    private fun setupMonsterList() {
        retrofit.create(DNDService::class.java).listMonsters()
            .enqueue(object : Callback<MonsterResponse> {
                override fun onFailure(call: Call<MonsterResponse>?, t: Throwable?) {
                    Log.v("retrofit", "call failed")
                }

                override fun onResponse(
                    call: Call<MonsterResponse>?,
                    response: Response<MonsterResponse>?
                ) {
                    Log.d("AllMonsters", response.toString())
                }
            })
    }

    private fun setupRightFabClick() {
        main_fab.setOnClickListener { view ->

            val encounterCRInput = EditText(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            encounterCRInput.layoutParams = lp
            encounterCRInput.inputType = InputType.TYPE_CLASS_NUMBER

            // TODO: spinner for EL instead of text?
            val sharedPref = getSharedPreferences("Dm-Otron", Context.MODE_PRIVATE)
            sharedPref.getInt("Party CR", 5)
            encounterCRInput.setText(sharedPref.getInt("Party CR", 5).toString())
            encounterCRInput.hint = getString(R.string.party_level_hint)
            var builder = AlertDialog.Builder(this)
            builder.setView(encounterCRInput)

            builder.setTitle("Create an Encounter! $DRAGON")
            builder.setPositiveButton(getString(R.string.let_roll)) { dialog, which ->
                // TODO: Match names to not create duplicates
                if (encounterCRInput.text.toString().isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "What are you a NPC?", Toast.LENGTH_LONG
                    ).show()
                    return@setPositiveButton
                } else if (Integer.parseInt(encounterCRInput.text.toString()) == 0) {
                    Toast.makeText(
                        applicationContext,
                        "What are you a commoner", Toast.LENGTH_LONG
                    ).show()
                    return@setPositiveButton
                }

                // Store Party CR
                val sharedPrefEdit = getSharedPreferences("Dm-Otron", Context.MODE_PRIVATE).edit()
                sharedPrefEdit.putInt(
                    "Party CR",
                    Integer.parseInt(encounterCRInput.text.toString())
                )
                sharedPrefEdit.apply()

                // Fetch monsters
                retrofit.create(DNDService::class.java).listMonsters()
                    .enqueue(object : Callback<MonsterResponse> {
                        override fun onFailure(call: Call<MonsterResponse>?, t: Throwable?) {
                            Log.v("retrofit", "call failed")
                        }

                        override fun onResponse(
                            call: Call<MonsterResponse>?,
                            response: Response<MonsterResponse>?
                        ) {
                            var allMonsters = response!!.body()!!.results
                            Log.v("Monster", allMonsters.get(0).name)
                            Log.v("Monster", allMonsters.get(1).name)
                            Log.v("Monster", allMonsters.get(2).name)
                            Log.v("Monster", allMonsters.get(3).name)

                            // do this until we get a monster whose CR is not above the party level
                            fetchIndividualMonster(
                                encounterCRInput.text.toString().toInt(),
                                allMonsters,
                                view
                            )
                        }
                    })
            }

            builder.setNegativeButton(getString(R.string.no_thanks))
            { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()
        }
    }

    private fun fetchIndividualMonster(
        encounterCR: Int,
        allMonsters: java.util.ArrayList<MonsterListing>,
        view: View
    ) {
        // get a random monster
        val randomMonster =
            allMonsters.get(RandomUtils.randInt(0, allMonsters.size - 1))
        val monsterId = randomMonster.getId()

        // Fetch individual monster
        retrofit.create(DNDService::class.java).getMonster(monsterId)
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

                    if (response?.body() == null) {
                        // @todo: throw error
                        return
                    } else {
                        val monster = response.body()
                        if (monster!!.challenge_rating == 0f) {
                            monster.challenge_rating = 1f
                        }

                        if (monster!!.challenge_rating > encounterCR) {
                            // we need to check again
                            return fetchIndividualMonster(
                                encounterCR,
                                allMonsters,
                                view
                            )
                        }
                        
                        Log.d("Chosen Monster", monster.name)
                        val numberOfMonsters =
                            encounterCR / monster.challenge_rating
                        val snackbarText = String.format(
                            "Encounter for Party Level " + encounterCR + ":\n"+ numberOfMonsters + " " + randomMonster.name + "s \n" + "Size = " + response!!.body()!!.size +"\n" + "AC = " + monster.armor_class + "\n" +"HP = " + monster.hit_points
                        )

                        Snackbar.make(
                            view,
                            snackbarText, Snackbar.LENGTH_LONG
                        ).show()

                        val experience = getEncounterXP(numberOfMonsters.toLong() * 2)

                        if (experience < 0) {
                            centertext.text =
                                "${centertext.text}${"$snackbarText (${getString(R.string.MAX)}) \n"}"
                        } else {
                            centertext.text =
                                "${centertext.text}${"$snackbarText ($experience) \n"}"
                        }

                        Snackbar.make(
                            view,
                            snackbarText, Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun getEncounterXP(cr: Long): Int {

        when (cr) {
            .125.toLong() -> return crXPLookup().get(0)
            .25.toLong() -> return crXPLookup().get(1)
            .5.toLong() -> return crXPLookup().get(2)
            1.toLong() -> return crXPLookup().get(3)
            2.toLong() -> return crXPLookup().get(4)
            else ->
                if (cr + 2 > crXPLookup().size) {
                    return -1
                } else {
                    return crXPLookup().get((cr + 2).toInt())
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun howManyMonstersforEncounter(party_level: Int, monstercr: Float): Number {
        // assume four party members and an average difficulty for now
        return ceil(party_level / monstercr).toInt()
    }

    // CR to XP converter
    fun crXPLookup(): ArrayList<Int> {
        return arrayListOf(
            25, // 1/8
            50, // 1/4
            100, // 1/2
            200, // 1
            450,
            700,
            1100,
            1800, // 5
            2300,
            2900,
            3900,
            5000,
            5900, // 10
            7200,
            8400,
            10000,
            11500,
            13000, // 15
            15000,
            18000,
            20000,
            22000,
            25000, // 20
            33000,
            41000,
            50000,
            62000,
            75000, // 25
            90000,
            105000,
            120000,
            135000,
            155000 // 30
        )
    }

    companion object {
        const val DRAGON = "🐉"
    }

    fun getRandomMonsterFromList(monsterList: ArrayList<Monster>): Monster {
        val rand = Random()
        val randomMonster = monsterList.get(rand.nextInt(monsterList.size))
        return randomMonster
    }

    fun getNumberOfMonsters(encounterCR: Int, monsterCR: Float): Number {
        val numberOfMonsters =
            howManyMonstersforEncounter(
                encounterCR,
                monsterCR
            )
        return numberOfMonsters
    }
}
