package com.miksxr.m_w_p.Tournaments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.miksxr.m_w_p.R
import com.miksxr.m_w_p.databinding.FragmentHistoryBinding

class History : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val navController = findNavController()

        binding.back.setOnClickListener { navController.navigate(R.id.tournamentNew) }



        //binding.match11.Check.text = "Рофло-турик(5)"
        //setCheckBoxListener(binding.match0.Check, binding.match0.Result)

        return binding.root
    }

    private fun setCheckBoxListener(checkBox: CheckBox, resultView: View) {
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            resultView.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }
}