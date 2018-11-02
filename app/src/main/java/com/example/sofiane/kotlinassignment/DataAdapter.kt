package com.example.sofiane.kotlinassignment

/**
 * Created by Sofiane on 11/2/2018.
 */
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.item_layout.view.*

class DataAdapter (private val dataList : ArrayList<RecipesJSON>, private val listener : Listener): RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    interface Listener {
        fun onItemClick(recipesJSON: RecipesJSON)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], listener)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(recipesJSON: RecipesJSON, listener: Listener) {

            itemView.recipe_description.text = recipesJSON.ingredients
            itemView.recipe_title.text = recipesJSON.title
            if( recipesJSON.thumbnail != "")
                Picasso.get().load(recipesJSON.thumbnail).fit().into(itemView.recipe_img)
            itemView.setOnClickListener{ listener.onItemClick(recipesJSON) }
        }

    }
}