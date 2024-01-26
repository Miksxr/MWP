package com.miksxr.m_w_p.Tournaments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.FragmentGrandNewBinding

class GrandNew : Fragment() {

    private lateinit var binding: FragmentGrandNewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGrandNewBinding.inflate(inflater, container, false)

        val navController = findNavController()

        binding.back.setOnClickListener { navController.navigate(R.id.tournamentNew) }

        for (i in 0 until 10) {
            val itemBinding = when (i) {
                0 -> binding.player1
                1 -> binding.player2
                2 -> binding.player3
                3 -> binding.player4
                4 -> binding.player5
                5 -> binding.player6
                6 -> binding.player7
                7 -> binding.player8
                8 -> binding.player9
                else -> binding.player10
            }
        }

        return binding.root
    }
}