package com.sharad.customlauncher

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.sharad.sdk.model.AppModel


/**
 * Created by Sharad-PC on 04-06-2021.
 */
class RecyclerViewAdapter(
    var context: Activity,
    var userArrayList: ArrayList<AppModel>?, var movieListFiltered: ArrayList<AppModel>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        movieListFiltered = userArrayList
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.row_app_items, parent, false)
        return RecyclerViewViewHolder(rootView)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val app = userArrayList?.get(position)
        val viewHolder = holder as RecyclerViewViewHolder
        viewHolder.appName.text = "Name     : ${app?.appName}"
        viewHolder.packageName.text = "Package  : ${app?.packageName}"
        viewHolder.activityName.text = "Activity : ${app?.activityName}"
        viewHolder.versionName.text = "Version  : ${app?.versionName}"
        viewHolder.versionCode.text = "Version  : ${app?.versionCode.toString()}"

        holder.appIcon.setImageDrawable(app?.appIcon)
        viewHolder.rootView.setOnClickListener({

            val pm = context.packageManager
            val launchIntent = pm.getLaunchIntentForPackage(app?.packageName!!)
            context.startActivity(launchIntent)
        })
    }

    override fun getItemCount(): Int {
        if (userArrayList != null) {
            return movieListFiltered!!.size
        } else {
            return 0
        }
    }

    internal inner class RecyclerViewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        var appName: TextView = itemView.findViewById(R.id.appName)
        var packageName: TextView = itemView.findViewById(R.id.packageName)
        var activityName: TextView = itemView.findViewById(R.id.activityName)
        var versionName: TextView = itemView.findViewById(R.id.versionName)
        var versionCode: TextView = itemView.findViewById(R.id.versionCode)
        var rootView: LinearLayout = itemView.findViewById(R.id.rootView)

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    movieListFiltered = userArrayList
                } else {
                    val filteredList: ArrayList<AppModel> = ArrayList()
                    for (movie in userArrayList!!) {
                        if (movie.appName!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(movie)
                        }
                    }
                    movieListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = movieListFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                movieListFiltered = filterResults.values as ArrayList<AppModel>
                notifyDataSetChanged()
            }
        }
    }


}