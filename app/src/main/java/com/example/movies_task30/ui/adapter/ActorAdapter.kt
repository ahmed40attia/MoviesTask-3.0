package com.example.movies_task30.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_task30.R
import com.example.movies_task30.data.model.movieActors.Cast
import com.example.movies_task30.databinding.ActorItemBinding
import com.example.movies_task30.listener.ActorListener


class ActorAdapter(showList: List<Cast>, private val movieLis: ActorListener) :
    RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {
    private val showList: List<Cast>
    private var layoutInflater: LayoutInflater? = null

    init {
        this.showList = showList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding: ActorItemBinding = DataBindingUtil.inflate(
            layoutInflater!!, R.layout.actor_item, parent, false
        )
        return ActorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bindTVShow(showList[position])

        holder.itemView.rootView.setOnClickListener {
            movieLis.onActorClick(showList[position].id)
        }

    }

    override fun getItemCount(): Int {
        return showList.size
    }

    inner class ActorViewHolder(binding: ActorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ActorItemBinding

        init {
            this.binding = binding
        }

        fun bindTVShow(cast:Cast) {
            binding.cast = cast
            binding.executePendingBindings()
        }
    }
}