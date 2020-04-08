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
    RecyclerView.Adapter<MonsterAdapter.MyViewHolder>() {

     var myDataset: ArrayList<MonsterListing> = ArrayList()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class MyViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        // create a new view
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.monster_layout, parent, false) as LinearLayout
        // set the view's size, margins, paddings and layout parameters

        return MyViewHolder(layout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.linearLayout.findViewById<TextView>(R.id.monster_name).text =
            myDataset[position].name
        holder.linearLayout.setOnClickListener { view ->
            val monsterId = myDataset[position].name
            val mIntent = Intent(holder.linearLayout.context, MonsterDetailActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString(MonsterDetailActivity.MONSTER_ID, monsterId)
            mIntent.putExtras(mBundle)
            startActivity(holder.linearLayout.context, mIntent, mBundle)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}