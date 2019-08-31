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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    // https://www.reddit.com/r/StrangePlanet/

    // https://fragmentedpodcast.com/

    // https://www.stilldrinking.org/programming-sucks

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
            // Do something for lollipop and above versions
//            centertext.
        } else {
            // do something for phones running an SDK before lollipop
        }
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Btncolor)))
        fab2.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Btncolor)))

        var rollhistory = ArrayList<Int>(0)


        fab.setOnClickListener { view ->
            var score = RandomUtils.randInt(1, 20)
//            Snackbar.make(view, String.format("You rolled a %d",score), Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()

            rollhistory.add(score)
            centertext.text = String.format("You rolled a %s \n", rollhistory.joinToString("\n You rolled a "))

        }
        fab2.setOnClickListener { view ->
            var builder = AlertDialog.Builder(this)

            val crinput = EditText(this)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            crinput.layoutParams = lp
            crinput.setInputType(
                InputType.TYPE_CLASS_NUMBER
            )
            // todo: spinner for EL?
            crinput.setText("")
            crinput.setHint(getString(R.string.monster_name_hint))

            builder.setView(crinput)

            val dragon = "🐉"
            builder.setTitle("Create an Encounter! $dragon")
            builder.setPositiveButton("Let's Roll(fuck yea)") { dialog, which ->
               // todo rn: grab random monster and figure out how many




                Toast.makeText(
                    applicationContext,
                    crinput.text, Toast.LENGTH_SHORT
                ).show()

                // TODO: Generate encounter
                // TODO: Match names to not create duplicates


            }
            builder.setNegativeButton("Let's not Roll(fuck No)") { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT
                ).show()
            }
            builder.show()
//            Snackbar.make(view, String.format("You rolled a %d",score), Snackbar.LENGTH_LONG)
//                .setAction("Action", nul
//                l).show()

//            rollhistory.add(score)
            centertext.text =
                String.format("You encountered a %s \n", rollhistory.joinToString("\n You encountered a "))

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
}
