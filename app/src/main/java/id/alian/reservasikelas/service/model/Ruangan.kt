package id.alian.reservasikelas.service.model

import com.google.gson.annotations.SerializedName

data class Ruangan(
    val id: Int? = null,
    @SerializedName("kode_ruangan")
    val kodeRuangan: String,
    val status: String,
)
