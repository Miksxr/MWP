package com.miksxr.m_w_p.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.RecyclerView.Adapter2
import com.miksxr.m_w_p.RecyclerView.Model2
import com.miksxr.m_w_p.databinding.FragmentStatisticsNewBinding


class StatisticsNew : Fragment() {

    private lateinit var binding: FragmentStatisticsNewBinding
    private lateinit var adapter2: Adapter2
    private lateinit var database: FirebaseFirestore

    private val numGames = 10
    private val GamesPick = mutableListOf<String>()
    private val WinratePick = mutableListOf<String>()
    private val GamesWin = mutableListOf<String>()
    private val WinrateWin = mutableListOf<String>()
    private val ImagePick = mutableListOf<Int>()
    private val ImageWin = mutableListOf<Int>()

    private var currentState = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsNewBinding.inflate(inflater, container, false)
        database = FirebaseFirestore.getInstance()

        binding.Filter.setOnClickListener {
            currentState = if (currentState == 0) 1 else 0

            when (currentState) {
                0 -> {
                    binding.State.text = "(Соло)"
                    binding.RVsolopick.visibility = View.VISIBLE
                    binding.RVsolowinrate.visibility = View.VISIBLE
                }
                1 -> {
                    binding.State.text = "(Пати)"
                    binding.RVsolopick.visibility = View.GONE
                    binding.RVsolowinrate.visibility = View.GONE
                }
            }
        }

        initRecyclerView(binding.RVpick, createDataList())
        initRecyclerView(binding.RVwinrate, createDataList())

        Firebase.firestore.collection("Statistics").document("Pick").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {
                for (i in 1..numGames) {
                    ImagePick.add(
                        resources.getIdentifier(snapshot.getString("Image$i"), "drawable", requireActivity().packageName)
                    )
                    GamesPick.add(snapshot.getString("Games$i").toString())
                    WinratePick.add(snapshot.getString("Win$i").toString())
                }

                initRecyclerView(binding.RVsolopick, createModelListSolo(true))
            }
        }

        Firebase.firestore.collection("Statistics").document("Winrate").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {
                for (i in 1..numGames) {
                    ImageWin.add(
                        resources.getIdentifier(snapshot.getString("Image$i"), "drawable", requireActivity().packageName)
                    )
                    GamesWin.add(snapshot.getString("Games$i").toString())
                    WinrateWin.add(snapshot.getString("Win$i").toString())
                }

                initRecyclerView(binding.RVsolowinrate, createModelListSolo(false))
            }
        }

        return binding.root
    }

    private fun initRecyclerView(recyclerView: RecyclerView, list: List<Model2>) {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter2 = Adapter2()
        recyclerView.adapter = adapter2
        adapter2.submitList(list)
    }

    private fun createModelListSolo(isPick: Boolean): List<Model2> {
        val list = mutableListOf<Model2>()
        val gamesList = if (isPick) GamesPick else GamesWin
        val winRateList = if (isPick) WinratePick else WinrateWin
        val imageList = if (isPick) ImagePick else ImageWin

        for (i in 0 until numGames) {
            list.add(Model2(imageList[i], gamesList[i], winRateList[i]))
        }

        return if (isPick) list.sortedByDescending { it.games.toInt() } else list.sortedByDescending { it.victories.replace("%", "").toInt() }
    }

    private fun createDataList(): List<Model2> {
        val dataList = mutableListOf<Model2>()

        for (i in 1..10) {
            dataList.add(Model2(
                image = R.drawable.question,
                games = "0",
                victories = "0%"
            ))
        }

        return dataList
    }
}