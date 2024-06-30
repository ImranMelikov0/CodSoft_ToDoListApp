package com.imranmelikov.codsoft_todolistapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.imranmelikov.codsoft_todolistapp.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {
    private lateinit var binding:FragmentTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentTaskBinding.inflate(inflater,container,false)
        return binding.root
    }
}