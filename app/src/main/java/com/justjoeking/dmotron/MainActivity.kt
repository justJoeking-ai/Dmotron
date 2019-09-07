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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {

    // https://www.reddit.com/r/StrangePlanet/
    // https://fragmentedpodcast.com/
    // https://www.stilldrinking.org/programming-sucks

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

        var monsterlist = ArrayList<Monster>(0)
        monsterlist.add(Monster("Goblin", .3f))
        monsterlist.add(Monster("Werewolf", 2f))
        monsterlist.add(Monster("Orc", 1f))
        monsterlist.add(Monster("Dragon", 8f))
        monsterlist.add(Monster("Lion", 3f))
        monsterlist.add(Monster("Giant Beetle", 3f))
        monsterlist.add(Monster("Shark", 2f))
        monsterlist.add(Monster("Hobgoblin", 1f))

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // Do something for lollipop and above versions centertext.
        } else {
            // do something for phones running an SDK before lollipop
        }
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

        var rollhistory = ArrayList<Int>(0)

        fab.setOnClickListener { view ->
            var score = RandomUtils.randInt(1, 20)
            rollhistory.add(score)
            centertext.text =
                String.format("You rolled a %s \n", rollhistory.joinToString("\n You rolled a "))
        }

        fab2.setOnClickListener { view ->
            var builder = AlertDialog.Builder(this)

            val encounterCRInput = EditText(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            encounterCRInput.layoutParams = lp
            encounterCRInput.inputType = InputType.TYPE_CLASS_NUMBER

            // TODO: spinner for EL?
            encounterCRInput.setText("")
            encounterCRInput.hint = getString(R.string.party_level_hint)
            builder.setView(encounterCRInput)
            builder.setTitle("Create an Encounter! $DRAGON")

            builder.setPositiveButton(getString(R.string.let_roll)) { dialog, which ->
                // TODO rn: grab random monster and figure out how many
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

                //get monsters
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://dnd5eapi.co/api/")
                    .build()

                val service = retrofit.create<DNDService>(DNDService::class.java)

                service.listMonsters()

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
                    getEncounterXP(numberOfMonsters.toLong() * randomMonster.fl.toLong())
                )}"
            }

            builder.setNegativeButton(getString(R.string.no_thanks)) { dialog, which ->
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
            .125.toLong() -> return crXPLookup.get(0)
            .25.toLong() -> return crXPLookup.get(1)
            .5.toLong() -> return crXPLookup.get(2)
            1.toLong() -> return crXPLookup.get(3)
            2.toLong() -> return crXPLookup.get(4)
            else -> return crXPLookup.get((cr + 2).toInt())
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

    // CR
    val crXPLookup = arrayListOf(
        25, // 1/8 0
        50,  // 1/4 1
        100, // 1/2 2
        200, // 1 3
        450,
        700,
        1100,
        1800,
        2300,
        2900,
        3900,
        5000,
        5900,
        7200,
        8400,
        10000,
        11500,
        13000, //15
        15000,
        18000,
        20000,
        22000,
        25000, //20
        33000,
        41000,
        50000,
        62000,
        75000,
        90000,
        105000,
        120000,
        135000,
        155000
    )

    companion object {
        const val DRAGON = "🐉"
    }

    fun getRandomMonsterFromList() {
        val rand = Random()
        val randomMonster = monsterlist.get(rand.nextInt(monsterlist.size))

        var numberOfMonsters =
            howManyMonstersforEncounter(
                Integer.parseInt(encounterCRInput.text.toString()),
                randomMonster.fl
            )


    }
}
