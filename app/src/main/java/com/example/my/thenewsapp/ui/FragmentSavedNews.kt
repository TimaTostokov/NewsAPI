package com.example.my.thenewsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my.thenewsapp.R
import com.example.my.thenewsapp.adapters.SavedArticleAdapter
import com.example.my.thenewsapp.mvvm.NewsDatabase
import com.example.my.thenewsapp.mvvm.NewsRepo
import com.example.my.thenewsapp.mvvm.NewsViewModel
import com.example.my.thenewsapp.mvvm.NewsViewModelFac

class FragmentSavedNews : Fragment(), MenuProvider {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: SavedArticleAdapter
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Saved News"

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)

        setHasOptionsMenu(true)

        rv = view.findViewById(R.id.rvSavedNews)
        val dao = NewsDatabase.getInstance(requireActivity()).newsDao
        val repository = NewsRepo(dao)
        val factory = NewsViewModelFac(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
        newsAdapter = SavedArticleAdapter()
        viewModel.getSavedNews.observe(viewLifecycleOwner, Observer {
            newsAdapter.setList(it)
            setUpRecyclerView()
        })
    }

    private fun setUpRecyclerView() {
        rv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu, menu)
        val searchIcon = menu.findItem(R.id.searchNews)
        val savedIcon = menu.findItem(R.id.savedNewsFrag)
        searchIcon.isVisible = false
        savedIcon.isVisible = false
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.deleteAll) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Menu")
            builder.setMessage("Are you sure to delete All saved articles")
            builder.setPositiveButton("Delete All") { _, _ ->
                viewModel.deleteAll()
                Toast.makeText(context, "Deleted All", Toast.LENGTH_SHORT).show()
                view?.findNavController()
                    ?.navigate(R.id.action_fragmentSavedNews_to_fragmentBreakingNews)
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }

        return true
    }

}