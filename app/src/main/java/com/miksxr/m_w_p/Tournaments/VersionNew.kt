package com.miksxr.m_w_p.Tournaments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.FragmentVersionNewBinding

class VersionNew : Fragment() {

    private lateinit var binding: FragmentVersionNewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentVersionNewBinding.inflate(inflater, container, false)

        val navController = findNavController()

        binding.back.setOnClickListener { navController.navigate(R.id.tournamentNew) }

        return binding.root
    }
}