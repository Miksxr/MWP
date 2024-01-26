package com.miksxr.m_w_p.Tournaments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.FragmentSoloNewBinding

class SoloNew : Fragment() {

    private lateinit var binding: FragmentSoloNewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSoloNewBinding.inflate(inflater, container, false)

        val navController = findNavController()

        binding.back.setOnClickListener { navController.navigate(R.id.tournamentNew) }

        setupRound("Round_One", listOf("Player1", "Player2", "Player3", "Player4", "Player5", "Player6", "Player7", "Player8"))
        setupRound("Round_Two", listOf("Player1", "Player2", "Player3", "Player4", "Player5", "Player6", "Player7", "Player8"))
        setupRound("Round_Three", listOf("Player1", "Player2", "Player3", "Player4", "Player5", "Player6"))
        setupRound("Round_Four", listOf("Player1", "Player2", "Player3", "Player4"))
        setupRound("Round_Five", listOf("Player1", "Player2"), true)

        Firebase.firestore.collection("Solo").document("Prize").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {
                binding.PrizeMoney.text = snapshot.getString("Text")
            }
        }

        Firebase.firestore.collection("Tournament").document("Solo_Date").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {
                binding.SoloDate.text = snapshot.getString("Text")
            }
        }

        return binding.root
    }

    private fun setupRound(roundName: String, playerIds: List<String>, isFinalRound: Boolean = false) {
        Firebase.firestore.collection("Solo").document(roundName).addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {
                val scoreIds = (1..playerIds.size).map { "Score$it" }

                for (i in playerIds.indices) {
                    val playerTextView =
                        binding.javaClass.getDeclaredField("Stage${roundName.replace("Round_", "")}${i + 1}").get(binding) as TextView
                    val scoreTextView =
                        binding.javaClass.getDeclaredField("Score${roundName.replace("Round_", "")}${i + 1}").get(binding) as TextView

                    val playerId = snapshot.getString(playerIds[i])
                    val score = snapshot.getString(scoreIds[i])

                    playerTextView.text = playerId
                    scoreTextView.text = score

                    if (isFinalRound && (score == "0" || score == "1" || score == "2")) {
                        playerTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                        playerTextView.setBackgroundResource(R.drawable.border2_dark)
                    } else if (!isFinalRound && (score == "0" || score == "1")) {
                        playerTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                        playerTextView.setBackgroundResource(R.drawable.border2_dark)
                    }
                }

                if (isFinalRound) {
                    val winnerTextView = binding.Winner
                    val roundNumber = roundName.replace("Round_", "")
                    val scoreText1 = (binding.javaClass.getDeclaredField("Score$roundNumber" + "1").get(binding) as TextView).text.toString()
                    val scoreText2 = (binding.javaClass.getDeclaredField("Score$roundNumber" + "2").get(binding) as TextView).text.toString()

                    if (scoreText1 == "3") {
                        winnerTextView.text = (binding.javaClass.getDeclaredField("Stage$roundNumber" + "1").get(binding) as TextView).text
                    } else if (scoreText2 == "3") {
                        winnerTextView.text = (binding.javaClass.getDeclaredField("Stage$roundNumber" + "2").get(binding) as TextView).text
                    } else {
                        winnerTextView.text = ""
                    }
                }
            }
        }
    }
}