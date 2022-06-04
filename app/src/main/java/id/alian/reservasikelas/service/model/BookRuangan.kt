package id.alian.reservasikelas.service.model

import com.google.gson.annotations.SerializedName

data class BookRuangan(
    val id: Int? = null,
    @SerializedName("id_ruangan")
    val idRuangan: Int,
    @SerializedName("id_matakuliah")
    val idMataKuliah: Int,
    @SerializedName("id_mahasiswa")
    val idMahasiswa: Int,
    @SerializedName("id_dosen")
    val idDosen: Int,
    val tanggal: String,
    @SerializedName("jam_awal")
    val jamAwal: String,
    @SerializedName("jam_akhir")
    val jamAkhir: String,
)