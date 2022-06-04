package id.alian.reservasikelas.service.model

import com.google.gson.annotations.SerializedName

data class Mahasiswa(
    val id: Int? = null,
    val nama: String,
    val nim: String,
    val email: String? = null,
    @SerializedName("id_kelas")
    val idKelas: Int,
    @SerializedName("id_jurusan")
    val idJurusan: Int,
    @SerializedName("tahun_angkatan")
    val tahunAngkatan: Int,
    val jurusan: Jurusan,
    val kelas: Kelas,
)
