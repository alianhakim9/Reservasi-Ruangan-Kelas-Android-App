package id.alian.reservasikelas.service.model

import com.google.gson.annotations.SerializedName

data class Kelas(
    val id: Int? = null,
    @SerializedName("kode_kelas")
    val kodeKelas: String,
    @SerializedName("tipe_kelas")
    val tipeKelas: String,
)
