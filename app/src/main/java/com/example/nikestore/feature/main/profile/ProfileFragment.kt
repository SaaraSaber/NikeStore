package com.example.nikestore.feature.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nikestore.NikeFragment
import com.example.nikestore.R
import com.example.nikestore.databinding.FragmentProfileBinding
import com.example.nikestore.feature.main.auth.AuthActivity
import com.example.nikestore.feature.main.favorite.FavoriteProductsActivity
import org.koin.android.ext.android.inject

class ProfileFragment : NikeFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.favoriteProductsBtn.setOnClickListener {
            startActivity(Intent(requireContext(), FavoriteProductsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }

    fun checkAuthState() {
        if (viewModel.isSignIn) {
            binding.authBtn.text = getString(R.string.signOut)
            binding.usernameTv.text = viewModel.userName
            binding.authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_user, 0)
            binding.authBtn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }
        } else {
            binding.authBtn.text = getString(R.string.signIn)
            binding.usernameTv.text = getString(R.string.guest_user)
            binding.authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            binding.authBtn.setOnClickListener {
                startActivity(Intent(requireContext(), AuthActivity::class.java))
            }
        }
    }
}