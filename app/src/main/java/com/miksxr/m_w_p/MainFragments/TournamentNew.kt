package com.miksxr.m_w_p.MainFragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.FragmentTournamentNewBinding

class TournamentNew : Fragment() {

    private lateinit var binding: FragmentTournamentNewBinding
    private var lastClickTime: Long = 0
    private var clickCount = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentTournamentNewBinding.inflate(inflater, container, false)

        val navController = findNavController()

        binding.buttonGrand.setOnClickListener { navController.navigate(R.id.grandNew) }
        binding.buttonSolo.setOnClickListener { navController.navigate(R.id.soloNew) }
        binding.buttonVersion.setOnClickListener { navController.navigate(R.id.versionNew) }
        binding.buttonZombi.setOnClickListener { navController.navigate(R.id.leonid) }
        binding.buttonHistory.setOnClickListener { navController.navigate(R.id.history) }

        binding.telegram.setOnClickListener { openLink("https://t.me/+XUpeObROpwE3ZjAy") }
        binding.discord.setOnClickListener { openLink("https://discord.gg/v8HYVUXma8") }
        binding.youtube.setOnClickListener { openLink("https://youtube.com/@miksxxxr?si=NMzPo55R12r9p5Ln") }
        binding.twitch.setOnClickListener { openLink("https://www.twitch.tv/miksxr_studio") }

        binding.buttonGames.setOnClickListener { binding.drawer.openDrawer(GravityCompat.START)}

        Firebase.firestore.collection("Tournament").document("News").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {

                binding.News.setImageResource(resources.getIdentifier(snapshot.getString("Image"), "drawable", requireActivity().packageName))
            }
        }

        Firebase.firestore.collection("Tournament").document("Solo_Date").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {

                binding.SoloDate.text = snapshot.getString("Text")
            }
        }

        binding.Dota2.setOnClickListener {
            val currentTime = System.currentTimeMillis()

            if (currentTime - lastClickTime < 1000) {
                clickCount++

                if (clickCount == 3) {
                    navController.navigate(R.id.leonid)
                    clickCount = 0
                }
            } else {
                clickCount = 1
            }

            lastClickTime = currentTime
        }

        return binding.root
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}