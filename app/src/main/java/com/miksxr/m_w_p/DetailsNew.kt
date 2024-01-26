package com.miksxr.m_w_p

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miksxr.m_w_p.RecyclerView.Adapter2
import com.miksxr.m_w_p.RecyclerView.Model2
import com.miksxr.m_w_p.databinding.FragmentDetailsNewBinding

class DetailsNew : Fragment() {

    private lateinit var binding: FragmentDetailsNewBinding
    private lateinit var adapter2: Adapter2
    private var currentState = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsNewBinding.inflate(inflater, container, false)

        val arguments = requireArguments()

        val navController = findNavController()

        binding.back.setOnClickListener { navController.navigate(R.id.ratingNew) }

        val image = arguments.getInt("image")
        val name = arguments.getString("name")
        val rating = arguments.getString("rating")
        val games = arguments.getString("games")
        val victories = arguments.getString("victories")

        binding.ImageImage.setImageResource(image)
        binding.TextName.text = name
        binding.TextRating.text = rating
        binding.TextGames.text = games
        binding.TextVictories.text = victories

        val victoriess = victories?.split("%")?.firstOrNull()?.toInt() ?: 0

        val colorRes = if (victoriess >= 50) R.color.green else R.color.red
        binding.TextVictories.setTextColor(ContextCompat.getColor(requireContext(), colorRes))

        if (victoriess >= 40) {
            binding.TextVictories.text = victories
        } else {
            binding.TextVictories.text = "<40%"
        }

        initRecyclerView(binding.RVpick, createDataList())
        initRecyclerView(binding.RVwinrate, createDataList())

        binding.filter.setOnClickListener {
            currentState = if (currentState == 0) 1 else 0

            when (currentState) {
                0 -> {
                    binding.Line.visibility = View.GONE
                    binding.State.text = "(Соло)"
                    binding.TextGames.visibility = View.VISIBLE
                    binding.TextVictories.visibility = View.VISIBLE
                    binding.TextRating.visibility = View.VISIBLE
                    binding.TextMaxRating.visibility = View.VISIBLE
                    binding.RVpick.visibility = View.GONE
                    binding.RVsolopick.visibility = View.VISIBLE
                    binding.RVwinrate.visibility = View.GONE
                    binding.RVsolowinrate.visibility = View.VISIBLE
                    binding.RVwinrate.visibility = View.GONE
                    binding.RVpick.visibility = View.GONE
                }
                1 -> {
                    binding.Line.visibility = View.VISIBLE
                    binding.State.text = "(Пати)"
                    binding.TextGames.visibility = View.INVISIBLE
                    binding.TextVictories.visibility = View.INVISIBLE
                    binding.TextRating.visibility = View.INVISIBLE
                    binding.TextMaxRating.visibility = View.INVISIBLE
                    binding.RVsolowinrate.visibility = View.GONE
                    binding.RVsolopick.visibility = View.GONE
                    binding.RVwinrate.visibility = View.VISIBLE
                    binding.RVpick.visibility = View.VISIBLE
                }
            }
        }

        getSampleDataSolo1()

        return binding.root
    }

    private fun getSampleDataSolo1(): ListenerRegistration {
        val textName = binding.TextName.text.toString()

        fun fetchData(playerName: String): ListenerRegistration {
            getSampleDataSolo(playerName, binding.RVsolopick, binding.RVsolowinrate)

            return Firebase.firestore.collection("Rating").document(playerName).addSnapshotListener { snapshot, exception ->
                if (exception == null && snapshot?.exists() == true) {
                    binding.TextMaxRating.text = snapshot.getString("MaxRt")
                }
            }
        }

        return when (textName) {
            "Iksar", "Miksxr", "Ko.Ka." -> fetchData(textName)
            else -> fetchData("Ko.Ka.")
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView, list: List<Model2>) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter2 = Adapter2()
        recyclerView.adapter = adapter2
        adapter2.submitList(list)
    }

    private fun getSampleDataSolo(playerName: String, pickRecyclerView: RecyclerView, winrateRecyclerView: RecyclerView) {
        val pickImageIds = mutableListOf<Int>()
        val pickGames = mutableListOf<String>()
        val pickWins = mutableListOf<String>()

        val winrateImageIds = mutableListOf<Int>()
        val winrateGames = mutableListOf<String>()
        val winrateWins = mutableListOf<String>()

        Firebase.firestore.collection("Rating").document(playerName).addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {
                for (i in 1..5) {
                    val pickImageName = snapshot.getString("Pick_Image$i") ?: "question"
                    val winrateImageName = snapshot.getString("Winrate_Image$i") ?: "question"

                    pickImageIds.add(
                        resources.getIdentifier(pickImageName, "drawable", requireActivity().packageName)
                    )
                    pickGames.add(snapshot.getString("Pick_Games$i").toString())
                    pickWins.add(snapshot.getString("Pick_Win$i").toString())

                    winrateImageIds.add(
                        resources.getIdentifier(winrateImageName, "drawable", requireActivity().packageName)
                    )
                    winrateGames.add(snapshot.getString("Winrate_Games$i").toString())
                    winrateWins.add(snapshot.getString("Winrate_Win$i").toString())
                }

                initRecyclerView(pickRecyclerView, getSampleData(pickImageIds, pickGames, pickWins))
                initRecyclerView(winrateRecyclerView, getSampleData(winrateImageIds, winrateGames, winrateWins))
            }
        }
    }

    private fun getSampleData(imageIds: List<Int>, games: List<String>, wins: List<String>): List<Model2> {
        return imageIds.mapIndexed { index, imageId ->
            Model2(imageId, games[index], wins[index])
        }.sortedByDescending { it.games.toInt() }
    }

    private fun createDataList(): List<Model2> {
        val dataList = mutableListOf<Model2>()

        for (i in 1..5) {
            dataList.add(Model2(
                image = R.drawable.question,
                games = "0",
                victories = "0%"
            ))
        }

        return dataList
    }

}