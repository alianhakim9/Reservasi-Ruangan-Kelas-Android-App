package id.alian.reservasikelas.service.model

import com.google.gson.annotations.SerializedName

data class MataKuliah(
    val id: Int? = null,
    @SerializedName("kode_matakuliah")
    val kodeMataKuliah: String,
    @SerializedName("nama_matakuliah")
    val namaMataKuliah: String,
    @SerializedName("jml_sks")
    val jumlahSks: Int,
    @SerializedName("jml_jam")
    val jumlahJam: Int,
    @SerializedName("id_kelas")
    val idKelas: Int,
    @SerializedName("id_dosen")
    val idDosen: Int,
)
