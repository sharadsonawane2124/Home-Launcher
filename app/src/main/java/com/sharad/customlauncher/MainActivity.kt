package com.sharad.customlauncher

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sharad.sdk.viewmodel.MainViewModel
import com.sharad.sdk.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity(), LifecycleOwner {
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var searchView: SearchView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val progressBar1 = findViewById<ProgressBar>(R.id.progressBar1)
        val viewModelFactory = ViewModelFactory(this)
        val mainViewModel = ViewModelProviders.of(this, viewModelFactory).get(
            MainViewModel::class.java
        )
        progressBar1.visibility = View.VISIBLE
        mainViewModel.appMutableLiveData.observe(this, Observer {
            progressBar1.visibility = View.GONE
            recyclerViewAdapter = RecyclerViewAdapter(this, it, it)
            recyclerView?.layoutManager = LinearLayoutManager(this)
            recyclerView?.adapter = recyclerViewAdapter
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView!!.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView!!.maxWidth = Int.MAX_VALUE
        searchView!!.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                recyclerViewAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                recyclerViewAdapter.filter.filter(query)
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_search) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView!!.isIconified) {
            searchView!!.isIconified = true
            return
        }
        super.onBackPressed()
    }
}