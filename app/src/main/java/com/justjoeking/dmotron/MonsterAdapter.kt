package com.justjoeking.dmotron

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class MonsterAdapter :
    RecyclerView.Adapter<MonsterAdapter.ViewHolder>() {

    var monsterList: ArrayList<MonsterListing> = ArrayList()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        // create a new view
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.monster_layout, parent, false) as LinearLayout

        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(layout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.linearLayout.findViewById<TextView>(R.id.monster_name).text =
            monsterList[position].name
        holder.linearLayout.setOnClickListener { view ->
            val monsterIndex = monsterList[position].index
            val mIntent = Intent(holder.linearLayout.context, MonsterDetailActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString(MonsterDetailActivity.MONSTER_ID, monsterIndex)
            mIntent.putExtras(mBundle)
            startActivity(holder.linearLayout.context, mIntent, mBundle)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = monsterList.size
}