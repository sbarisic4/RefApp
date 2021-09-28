package com.stjepanbarisic.refapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stjepanbarisic.refapp.models.Game
import com.stjepanbarisic.refapp.databinding.FragmentGamesBinding
import com.stjepanbarisic.refapp.ui.activites.MapsActivity
import com.stjepanbarisic.refapp.ui.adapters.GameAdapter
import com.stjepanbarisic.refapp.viewmodels.GamesViewModel
import org.koin.android.viewmodel.ext.android.viewModel


internal const val BUNDLE_KEY_HOST_FOOTBALL_TEAM = "host_football_team"

class GamesFragment : Fragment(), GameAdapter.OnGameClickListener {

    private lateinit var binding: FragmentGamesBinding
    private val gamesViewModel: GamesViewModel by viewModel()

    private var gameAdapter: GameAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gamesViewModel.gamesOptionsLD.observe(this, { options ->
            gameAdapter = GameAdapter(options, this@GamesFragment)
            binding.rvGames.adapter = gameAdapter
            gameAdapter?.startListening()
        })

        gamesViewModel.toastLD.observe(this, { message ->
            if (!message.isNullOrEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                gamesViewModel.resetToast()
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvGames.adapter = gameAdapter
        binding.rvGames.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
    }

    override fun onDeleteClick(game: Game, position: Int) {
        gameAdapter?.deleteItem(position)
    }

    override fun onLocationClick(hostFootballTeamName: String) {
        val hostFootballTeam = gamesViewModel.getFootballTeamByName(hostFootballTeamName)
        startMapActivity(bundleOf(BUNDLE_KEY_HOST_FOOTBALL_TEAM to hostFootballTeam))
    }

    private fun startMapActivity(args: Bundle) {
        val intent = Intent(requireContext(), MapsActivity::class.java)
        intent.putExtras(args)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        gameAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        gameAdapter?.stopListening()

    }
}