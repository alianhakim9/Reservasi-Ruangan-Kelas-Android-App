package id.alian.reservasikelas.service.model

import com.google.gson.annotations.SerializedName

data class Jurusan(
    val id: Int? = null,
    @SerializedName("kode_jurusan")
    val kodeJurusan: String,
    @SerializedName("nama_jurusan")
    val namaJurusan: String,
    val idProdi: Int? = null,
)
