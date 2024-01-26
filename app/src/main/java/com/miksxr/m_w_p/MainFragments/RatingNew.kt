package com.miksxr.m_w_p.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.RecyclerView.Adapter
import com.miksxr.m_w_p.RecyclerView.Model
import com.miksxr.m_w_p.databinding.FragmentRatingNewBinding

class RatingNew : Fragment(), Adapter.Listener {

    private lateinit var binding: FragmentRatingNewBinding
    private lateinit var firestore: FirebaseFirestore
    private var adapter = Adapter(this)

    private val players = listOf(
        Player("Miksxr", "MiksxrRT", "MiksxrGames", "MiksxrWin", R.drawable.miksxr),
        Player("Iksar", "IksarRT", "IksarGames", "IksarWin", R.drawable.iksar),
        Player("Ko.Ka.", "KoKaRT", "KoKaGames", "KoKaWin", R.drawable.koka),
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRatingNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.RV) {
            layoutManager = LinearLayoutManager(activity)
            adapter = Adapter(this@RatingNew)
        }

        firestore = FirebaseFirestore.getInstance()
        binding.RV.adapter = adapter

        players.forEach { player ->
            val documentReference = firestore.collection("Rating").document(player.name)

            documentReference.addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    player.updateRT(snapshot.getString("Rt") ?: "")
                    player.updateGames(snapshot.getString("Games") ?: "")
                    player.updateWin(snapshot.getString("Win") ?: "")
                    updateList()
                }
            }
        }
    }

    override fun onClick(item: Model) {
        val bundle = Bundle().apply {
            putInt("image", item.image)
            putString("name", item.name)
            putString("rating", item.rating)
            putString("games", item.games)
            putString("victories", item.victories)
        }
        val navController = findNavController()
        navController.navigate(R.id.detailsNew, bundle)
    }

    private fun updateList() {
        val sortedList = players.map { it.toModel() }.sortedByDescending {
            val ratingValue = it.rating.replace("rt", "").takeIf { it.isNotEmpty() }?.toIntOrNull() ?: Int.MIN_VALUE
            ratingValue
        }
        adapter.submitList(sortedList)
    }

    data class Player(
        val name: String,
        val rtReference: String,
        val gamesReference: String,
        val winReference: String,
        val imageResource: Int
    ) {
        var rt: String = ""
        var games: String = ""
        var win: String = ""

        fun toModel() = Model(imageResource, name, rt, games, win)

        fun updateRT(newValue: String) {
            rt = newValue
        }

        fun updateGames(newValue: String) {
            games = newValue
        }

        fun updateWin(newValue: String) {
            win = newValue
        }
    }
}
