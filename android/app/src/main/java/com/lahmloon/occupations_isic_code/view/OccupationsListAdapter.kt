package com.lahmloon.occupations_isic_code.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lahmloon.occupations_isic_code_sdk.occupations.data.Occupations
import com.lahmloon.occupations_isic_code.databinding.ItemOccupationBinding
import kotlin.properties.Delegates

class OccupationListAdapter(private val selectionListener: (occupations: Occupations) -> Unit): RecyclerView.Adapter<OccupationViewHolder>() {

    var occupations: List<Occupations> by Delegates.observable(emptyList()) { _, old, new ->
        DiffUtil.calculateDiff(OccupationDiff(old, new), false).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OccupationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOccupationBinding.inflate(inflater, parent, false)
        val viewHolder = OccupationViewHolder(binding)
        binding.root.setOnClickListener{
            selectionListener(viewHolder.occupationsClicked!!)
        }
        return viewHolder
    }

    override fun getItemCount(): Int = occupations.size

    override fun onBindViewHolder(holder: OccupationViewHolder, position: Int) {
        holder.bind(occupations[position])
    }
}

private class OccupationDiff(private val old: List<Occupations>, private val aNew: List<Occupations>):DiffUtil.Callback() {
    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = aNew.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition].id == aNew[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = aNew[newItemPosition]
        return oldItem.nameTh == newItem.nameTh && oldItem.nameEng == newItem.nameEng
    }

}

class OccupationViewHolder(private val binding: ItemOccupationBinding): RecyclerView.ViewHolder(binding.root) {

    var occupationsClicked: Occupations? = null

    fun bind(occupations: Occupations) {
        binding.occupationsId.text = occupations.id
        binding.occupationsName.text = occupations.nameTh
        occupationsClicked = occupations
    }
}