package com.example.githubuser.data.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.adapter.ListUserAdapter
import com.example.githubuser.data.response.User
import com.example.githubuser.databinding.FragmentFollowersBinding


class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val fBinding get() = _binding!!
    private lateinit var listAdapter: ListUserAdapter
    private lateinit var followerViewModel: AllViewModels
    private lateinit var username: String
    private lateinit var name: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater,container, false)
        return _binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        name = args?.getString(DetailActivity.EXTRA_NAME).toString()
        username = args?.getString(DetailActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowersBinding.bind(view)

        listAdapter = ListUserAdapter()
        listAdapter.notifyDataSetChanged()

        fBinding.apply {
            FollowerItem.setHasFixedSize(true)
            FollowerItem.layoutManager = LinearLayoutManager(activity)
            FollowerItem.adapter = listAdapter
        }

        listAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("username", user.login)
                intent.putExtra("name", user.name)
                startActivity(intent)
            }
        })

        showLoading(true)
        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            AllViewModels::class.java)
        followerViewModel.getSetFollowers(username)
        followerViewModel.followers().observe(viewLifecycleOwner) { followers ->
            if (followers != null) {
                Log.d("FollowersFragment", "Followers received: $followers")
                listAdapter.updateData(followers)
                showLoading(false)
            } else {
                Log.e("FollowersFragment", "Failed to receive followers")
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            fBinding.FollowerLoading.visibility = View.VISIBLE
        } else {
            fBinding.FollowerLoading.visibility = View.GONE
        }
    }
}