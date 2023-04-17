package com.example.replybot.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.example.replybot.data.model.Rules
import com.example.replybot.databinding.ItemLayoutRulesBinding

class RulesAdapter(
    private var items: List<Rules>,
    val onClick: (item: Rules) -> Unit,
    val onClick2: (item: Rules) -> Unit,
    val onClick3: (item:Rules) -> Unit
) : RecyclerView.Adapter<RulesAdapter.ItemRulesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRulesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutRulesBinding.inflate(layoutInflater, parent, false)
        return ItemRulesHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemRulesHolder, position: Int) {
        val item = items[position]
        holder.binding.run {
            tvTitle.text = item.name
            tvMeaning.text = item.reply
            icTrash.setOnClickListener {
                onClick(item)
            }
            llEdit.setOnClickListener{
                onClick2(item)
            }
            icActivation.setOnClickListener {
                onClick3(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setRules(items: List<Rules>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ItemRulesHolder(val binding: ItemLayoutRulesBinding) :
        RecyclerView.ViewHolder(binding.root)
}
