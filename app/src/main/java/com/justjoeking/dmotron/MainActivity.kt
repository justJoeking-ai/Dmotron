package com.justjoeking.dmotron

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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.collections.ArrayList
import kotlin.math.ceil
import retrofit2.Retrofit
import android.util.Log
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

        fab.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    this,
                    R.color.Btncolor
                )
            )
        )
        fab2.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    this,
                    R.color.Btncolor
                )
            )
        )

        setupMonsterList()
        setupLeftFabClick()
        setupRightFabClick()
    }

    private fun setupMonsterList() {
        retrofit.create<DNDService>(DNDService::class.java).listMonsters()
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

    private fun setupLeftFabClick() {
        fab.setOnClickListener { view ->
            var score = RandomUtils.randInt(1, 20)
            rollhistory.add(score)
            centertext.text =
                String.format("You rolled a %s \n", rollhistory.joinToString("\n You rolled a "))
        }
    }


    private fun setupRightFabClick() {
        fab2.setOnClickListener { view ->

            val encounterCRInput = EditText(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            encounterCRInput.layoutParams = lp
            encounterCRInput.inputType = InputType.TYPE_CLASS_NUMBER

            // TODO: spinner for EL instead of text?
            encounterCRInput.setText("")
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

                // Fetch monsters
                retrofit.create<DNDService>(DNDService::class.java).listMonsters()
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

                            val randomMonster = allMonsters.get(RandomUtils.randInt(0, allMonsters.size))


                            val numberOfMonsters = 4
                            val snackbarText = String.format(
                                "Encounter: " + numberOfMonsters + " " + randomMonster.name + "s",
                                Snackbar.LENGTH_LONG
                            )

                            Snackbar.make(
                                view,
                                snackbarText, Snackbar.LENGTH_LONG
                            ).show()

                            centertext.text = "${centertext.text}${String.format(
                                "%s (%s) \n",
                                snackbarText,
                                getEncounterXP(numberOfMonsters.toLong() * 2)
                            )}"
                        }
                    })

                //Fetch Spells
                var allSpells: List<SpellListing>
                retrofit.create<DNDService>(DNDService::class.java).listSpell()
                    .enqueue(object : Callback<SpellResponse> {
                        override fun onFailure(call: Call<SpellResponse>?, t: Throwable?) {
                            Log.v("retrofit", "call failed")
                        }

                        // fetch spell listing, use it to get a spell
                        override fun onResponse(
                            call: Call<SpellResponse>?,
                            response: Response<SpellResponse>?
                        ) {
                            allSpells = response!!.body()!!.results!!
                            Log.v("Spell", allSpells.get(0).name)
                            Log.v("Spell", allSpells.get(1).name)
                            Log.v("Spell", allSpells.get(2).name)

//                            val numberOfSpells = 4
//                            val snackbarText = String.format(
//                                "Encounter: " + numberOfSpells + " " + allSpells.name + "s",
//                                Snackbar.LENGTH_LONG
//                            )

//                            Snackbar.make(
//                                view,
//                                snackbarText, Snackbar.LENGTH_LONG
//                            ).show()

                            val randomSpell = allSpells.get(RandomUtils.randInt(0, allSpells.size))

                            centertext.text = "${centertext.text}${String.format(
                                "(with a scroll of %s) \n",
                                randomSpell.name
                            )}"
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

    private fun getEncounterXP(cr: Long): Int {
        // todo return xp level

        when (cr) {
            .125.toLong() -> return crXPLookup().get(0)
            .25.toLong() -> return crXPLookup().get(1)
            .5.toLong() -> return crXPLookup().get(2)
            1.toLong() -> return crXPLookup().get(3)
            2.toLong() -> return crXPLookup().get(4)
            else -> return crXPLookup().get((cr + 2).toInt())
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
