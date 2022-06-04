package id.alian.reservasikelas.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.alian.reservasikelas.databinding.ItemRuanganBinding
import id.alian.reservasikelas.service.model.Ruangan

class ListRuanganAdapter : RecyclerView.Adapter<ListRuanganAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemRuanganBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Ruangan>() {
        override fun areItemsTheSame(oldItem: Ruangan, newItem: Ruangan): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Ruangan, newItem: Ruangan): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRuanganBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ruangans = differ.currentList[position]

        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let {
                    it(ruangans)
                }
            }
        }

        with(holder.binding) {
            namaRuanganTextView.text = ruangans.kodeRuangan
            statusRuanganTextView.text = ruangans.status
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Ruangan) -> Unit)? = null

    fun setOnItemClickListener(listener: (Ruangan) -> Unit) {
        onItemClickListener = listener
    }
}