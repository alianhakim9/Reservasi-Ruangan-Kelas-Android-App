package id.alian.reservasikelas.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.alian.reservasikelas.databinding.ItemReservasiBinding
import id.alian.reservasikelas.databinding.ItemRuanganBinding
import id.alian.reservasikelas.service.model.BookRuangan
import id.alian.reservasikelas.service.model.Ruangan

class ListBookRuanganAdapter : RecyclerView.Adapter<ListBookRuanganAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemReservasiBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<BookRuangan>() {
        override fun areItemsTheSame(oldItem: BookRuangan, newItem: BookRuangan): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: BookRuangan, newItem: BookRuangan): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReservasiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookRuangan = differ.currentList[position]

        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let {
                    it(bookRuangan)
                }
            }
        }

        with(holder.binding) {
            idBookTextView.text = bookRuangan.idBook.toString()
            kodeRuanganTextView.text = bookRuangan.ruangan?.kodeRuangan
            kodeMataKuliahTextView.text =
                "${bookRuangan.mataKuliah?.kodeMataKuliah} | ${bookRuangan.tanggal}"
            jamAwalTextView.text = "${bookRuangan.jamAwal} : ${bookRuangan.jamAkhir}"
            nimTextView.text = "${bookRuangan.mahasiswa?.nim} | ${bookRuangan.dosen?.kodeDosen}"
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((BookRuangan) -> Unit)? = null

    fun setOnItemClickListener(listener: (BookRuangan) -> Unit) {
        onItemClickListener = listener
    }
}