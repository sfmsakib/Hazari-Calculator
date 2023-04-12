package com.sprout.hazaricalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(dataList:List<Score>): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var listScore: List<Score> = dataList

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var value1: TextView
        var value2: TextView
        var value3: TextView
        var value4: TextView
        init {

            value1 = itemView.findViewById<TextView>(R.id.value1)
            value2 = itemView.findViewById<TextView>(R.id.value2)
            value3 = itemView.findViewById<TextView>(R.id.value3)
            value4 = itemView.findViewById<TextView>(R.id.value4)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_dashboard,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listScore.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.value1.text = listScore[position].player1Score.toString()
        holder.value2.text = listScore[position].player2Score.toString()
        holder.value3.text = listScore[position].player3Score.toString()
        holder.value4.text = listScore[position].player4Score.toString()
    }
}