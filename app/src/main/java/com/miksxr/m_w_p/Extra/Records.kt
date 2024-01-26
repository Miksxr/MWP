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
import com.miksxr.m_w_p.databinding.FragmentRecordsBinding

class Records : Fragment() {

    private lateinit var binding: FragmentRecordsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecordsBinding.inflate(inflater, container, false)

        val navController = findNavController()

        binding.back.setOnClickListener { navController.navigate(R.id.extraNew) }

        val textResources = arrayOf(
            "Максимум \nдобитых крипов", "Максимум \nзаденаеных крипов", "Максимум \nпоставленных вардов",
            "Максимум \nразрушенных вардов", "Максимум \nзаработанного золота", "Максимум \nзаработанного опыта"
        )

        val bindingList = listOf(
            binding.rec1, binding.rec2, binding.rec3, binding.rec4, binding.rec5, binding.rec6
        )

        for (i in 0 until 6) {
            bindingList[i].label.text = textResources[i]
        }

        val textResources4 = arrayOf("Самый \nдлинный матч", "Самый \nкороткий матч")
        val bindingList4 = listOf(binding.rec7, binding.rec8)

        for (i in 0 until 2) {
            bindingList4[i].label.text = textResources4[i]
        }

        Firebase.firestore.collection("Extra").document("Records").addSnapshotListener { snapshot, exception ->
            if (exception == null && snapshot?.exists() == true) {

                for (i in 1..6) {
                    val recordBinding = bindingList[i - 1]
                    recordBinding.photo.setImageResource(resources.getIdentifier(
                        snapshot.getString("Image$i"), "drawable", requireActivity().packageName
                    ))
                    recordBinding.name.text = snapshot.getString("Player$i")
                    recordBinding.record.text = "${snapshot.getString("Record$i")} \n${getRecordUnit(i)}"
                }

                binding.rec7.tournament.text = snapshot.getString("Tournament7")
                binding.rec8.tournament.text = snapshot.getString("Tournament8")

                bindingList4[0].record.text = "${snapshot.getString("Record7")} минуты"
                bindingList4[1].record.text = "${snapshot.getString("Record8")} минуты"
            }
        }

        return binding.root
    }

    private fun getRecordUnit(index: Int): String {
        return when (index) {
            1, 2 -> "крипов"
            3, 4 -> "вардов"
            5 -> "золота"
            6 -> "опыта"
            else -> ""
        }
    }
}