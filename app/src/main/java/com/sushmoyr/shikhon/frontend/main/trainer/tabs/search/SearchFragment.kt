package com.sushmoyr.shikhon.frontend.main.trainer.tabs.search

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sushmoyr.shikhon.R
import com.sushmoyr.shikhon.databinding.FragmentSearchBinding
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.viewpagertabs.PostSearchResultFragment
import com.sushmoyr.shikhon.frontend.main.trainer.tabs.search.viewpagertabs.UserSearchResultFragment

class SearchFragment : Fragment(), SearchView.OnQueryTextListener {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val model: SearchViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        model.filteredPosts.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()){

                Log.d("searchingFeature", "******* Search Post Data of size: ${it.size} Returned ******")
                it.forEach {
                    Log.d("searchingFeature", it.toString())
                }

            }
        })

        model.filteredUser.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()){

                Log.d("searchingFeature", "******* Search User Data Returned ******")
                it.forEach {
                    Log.d("searchingFeature", it.toString())
                }

            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbarSearch
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Search"
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)



        val fragmentList = arrayListOf<Fragment>(
            PostSearchResultFragment(),
            UserSearchResultFragment()
        )

        val adapter = SearchViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.searchViewPager.adapter = adapter

        val tabLayout = binding.tabs
        val viewPager = binding.searchViewPager
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            when(position){
                0-> tab.text = "Posts"
                1-> tab.text = "Users"
            }
        }.attach()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("searchingFeature", "Query")
        model.queryData(query!!.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("searchingFeature", newText.toString())
        model.queryData(newText!!.toString())
        return false
    }

}
