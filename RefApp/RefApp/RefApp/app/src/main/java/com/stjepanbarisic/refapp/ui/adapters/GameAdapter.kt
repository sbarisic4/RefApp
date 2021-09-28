package com.stjepanbarisic.refapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.stjepanbarisic.refapp.R
import com.stjepanbarisic.refapp.models.Game
import com.stjepanbarisic.refapp.databinding.ItemGameBinding
import java.text.SimpleDateFormat

class GameAdapter(
    options: FirebaseRecyclerOptions<Game>?, private val listener: OnGameClickListener
) : FirebaseRecyclerAdapter<Game, GameViewHolder>(options!!) {

    interface OnGameClickListener {
        fun onDeleteClick(game: Game, position: Int)
        fun onLocationClick(hostFootballTeamName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int, model: Game) {
        holder.bind(model, position, listener)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).ref.removeValue()
    }

}


class GameViewHolder(private val binding: ItemGameBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        game: Game,
        position: Int,
        listener: GameAdapter.OnGameClickListener
    ) {
        binding.apply {

            tvHostTeamName.text = game.hostTeam

            tvGuestTeamName.text = game.guestTeam

            tvStartTime.text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(game.startTime)

            tvRefereeName.text = itemView.resources.getString(R.string.referee, game.referee.name)

            btnDeleteGame.setOnClickListener {
                listener.onDeleteClick(game, position)
            }

            btnLocationOfGame.setOnClickListener {
                listener.onLocationClick(game.hostTeam)
            }
        }
    }

}