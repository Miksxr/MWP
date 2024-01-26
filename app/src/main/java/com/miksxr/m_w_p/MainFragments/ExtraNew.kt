package com.miksxr.m_w_p.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.FragmentExtraNewBinding

class ExtraNew : Fragment() {

    private val binding by lazy { FragmentExtraNewBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val navController = findNavController()

        binding.buttonRules.setOnClickListener { navController.navigate(R.id.rulesNew) }
        binding.buttonMore.setOnClickListener { navController.navigate(R.id.records) }

        Firebase.firestore.collection("Extra").document("Results").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {
                updateUIWithFirestoreData(snapshot)
            }
        }

        return binding.root
    }

    private fun updateUIWithFirestoreData(snapshot: DocumentSnapshot) {
        binding.Tournaments.text = snapshot.getString("Tournaments")
        binding.Streams.text = snapshot.getString("Streams")
        binding.Videos.text = snapshot.getString("Videos")
        binding.Players.text = snapshot.getString("Players")
        binding.Money.text = snapshot.getString("Money")
    }
}