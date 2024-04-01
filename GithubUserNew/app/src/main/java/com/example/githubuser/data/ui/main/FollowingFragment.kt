package com.example.githubuser.data.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.adapter.ListUserAdapter
import com.example.githubuser.data.response.User
import com.example.githubuser.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val fragmentFollowingbinding get() = _binding!!
    private lateinit var listAdapter: ListUserAdapter
    private lateinit var followingviewModel: AllViewModels
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater,container, false)
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowingBinding.bind(view)

        listAdapter = ListUserAdapter()
        listAdapter.notifyDataSetChanged()

        fragmentFollowingbinding.apply {
            FollowingItem.setHasFixedSize(true)
            FollowingItem.layoutManager = LinearLayoutManager(activity)
            FollowingItem.adapter = listAdapter
        }

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("username", user.login)
                startActivity(intent)
            }
        })

        showLoading(true)
        followingviewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            AllViewModels::class.java
        )
        followingviewModel.getSetFollowing(username)
        followingviewModel.following().observe(viewLifecycleOwner) {
            if (it != null) {
                listAdapter.updateData(it)
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fragmentFollowingbinding.FollowingLoading.visibility = View.VISIBLE
        } else {
            fragmentFollowingbinding.FollowingLoading.visibility = View.GONE
        }
    }
}