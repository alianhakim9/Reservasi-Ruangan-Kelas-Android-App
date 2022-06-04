package id.alian.reservasikelas.service.model

import com.google.gson.annotations.SerializedName

data class Dosen(
    val id: Int? = null,
    @SerializedName("kode_dosen")
    val kodeDosen: String,
    val nama: String,
    val email: String,
    val nidn: String,
)
