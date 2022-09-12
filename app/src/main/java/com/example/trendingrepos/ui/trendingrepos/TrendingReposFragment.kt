package com.example.trendingrepos.ui.trendingrepos

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.example.trendingrepos.R
import com.example.trendingrepos.component.toast
import com.example.trendingrepos.data.ApiResult
import com.example.trendingrepos.data.TrendingRepo
import com.example.trendingrepos.databinding.FragmentTrendingReposBinding
import com.example.trendingrepos.ui.trendingrepos.adapter.TrendingReposAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrendingReposFragment : Fragment(R.layout.fragment_trending_repos),
    SearchView.OnQueryTextListener {
    /** Declare adapter instance */
    private lateinit var trendingRepoAdapter: TrendingReposAdapter

    /** Bind viewModel to activity */
    private val viewModel: TrendingReposViewModel by viewModels()
    private lateinit var binding: FragmentTrendingReposBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrendingReposBinding.bind(view)
        viewModel.repos.observe(viewLifecycleOwner, trendingRepoObserver)
        // Add menu items without using the Fragment Menu APIs
        activity?.addMenuProvider(menuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
        // Retry in case of failure
        binding.btnRetry.setOnClickListener { viewModel.retry() }
    }

    /** Observe network api result and update the status on view */
    private val trendingRepoObserver = Observer<ApiResult<out List<TrendingRepo>>> { apiResult ->
        when (apiResult) {
            //Request in loading state
            is ApiResult.Loading -> {
                binding.progress.isVisible = true
            }
            //Get success result
            is ApiResult.Success -> {
                binding.progress.isVisible = false
                initView(apiResult.data?.toMutableList() ?: mutableListOf())
            }
            //Get the failure
            is ApiResult.Error -> {
                binding.apply {
                    toast(apiResult.message ?: resources.getString(R.string.failed_msg))
                    progress.isVisible = false
                    error.isVisible = true
                    error.text = apiResult.message
                    btnRetry.isVisible = true
                }
            }
        }
    }

    /** Init recyclerview and adapter to inflate item on list
     * @param itemList: Trending github repos list
     */
    private fun initView(itemList: MutableList<TrendingRepo>) {
        binding.apply {
            trendingRepoAdapter = TrendingReposAdapter(itemList) { itemPos ->
                viewModel.updateSelectedItemStatus(itemPos) { updatedList ->
                    trendingRepoAdapter.updateSelectedItem(updatedList)
                }
            }
            this.recyclerview.apply {
                setHasFixedSize(true)
                itemAnimator = null
                postponeEnterTransition()
                this.adapter = trendingRepoAdapter
            }
        }
    }

    /** Inflate MenuItems to the component owning the app bar */
    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            // Add menu items here
            menuInflater.inflate(R.menu.menu_repos, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            // Handle the menu selection
            return when (menuItem.itemId) {
                R.id.action_search -> {
                    (menuItem.actionView as SearchView).setOnQueryTextListener(this@TrendingReposFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { searchQuery ->
            //Request for filtered list
            viewModel.filterTrendingRepoList(searchQuery) { filteredList ->
                //Validate recyclerview adapter class instance created or not
                if (this::trendingRepoAdapter.isInitialized) {
                    //Update selected item with recyclerview adapter
                    trendingRepoAdapter.apply {
                        updateSelectedItem(filteredList)
                    }
                }
            }
        }
        return true
    }
}