package com.stjepanbarisic.refapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stjepanbarisic.refapp.databinding.FragmentRefereesBinding
import com.stjepanbarisic.refapp.ui.adapters.RefereeAdapter
import com.stjepanbarisic.refapp.viewmodels.RefereesViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RefereesFragment : Fragment() {

    private lateinit var binding: FragmentRefereesBinding
    private val refereesViewModel: RefereesViewModel by viewModel()
    private val refereeAdapter = RefereeAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRefereesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvReferees.adapter = refereeAdapter
        binding.rvReferees.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        refereesViewModel.refereesLD.observe(this, { list ->
            if (!list.isNullOrEmpty()) {
                refereeAdapter.loadNewReferees(list)
            }
        })

        refereesViewModel.toastLD.observe(this, { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                refereesViewModel.resetToast()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        refereesViewModel.refreshReferees()
    }

}