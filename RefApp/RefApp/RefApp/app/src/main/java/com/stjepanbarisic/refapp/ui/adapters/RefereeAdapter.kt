package com.stjepanbarisic.refapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stjepanbarisic.refapp.models.RefereeWithGames
import com.stjepanbarisic.refapp.databinding.ItemRefereeBinding

class RefereeAdapter(
    private var refereesWithGames: List<RefereeWithGames>,
) :
    RecyclerView.Adapter<RefereeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RefereeViewHolder {
        val binding = ItemRefereeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return RefereeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RefereeViewHolder, position: Int) {
        holder.bind(refereesWithGames[position])
    }

    override fun getItemCount(): Int {
        return refereesWithGames.size
    }

    fun loadNewReferees(newReferees: List<RefereeWithGames>) {
        refereesWithGames = newReferees
        notifyDataSetChanged()
    }

}

class RefereeViewHolder(private val binding: ItemRefereeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        refereeWithGames: RefereeWithGames
    ) {
        binding.tvRefereeName.text = refereeWithGames.referee.name
        binding.tvGamesNumber.text = refereeWithGames.games.size.toString()
    }

}