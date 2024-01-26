package com.miksxr.m_w_p.Extra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.FragmentRulesNewBinding

class RulesNew : Fragment() {

    private lateinit var binding: FragmentRulesNewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRulesNewBinding.inflate(inflater, container, false)

        val navController = findNavController()

        binding.back.setOnClickListener { navController.navigate(R.id.extraNew) }

        Firebase.firestore.collection("Extra").document("Rules").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {

                binding.TextRules.text = snapshot.getString("Text")
            }
        }

        return binding.root
    }
}