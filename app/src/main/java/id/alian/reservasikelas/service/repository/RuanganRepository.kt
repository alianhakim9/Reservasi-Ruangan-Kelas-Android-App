package id.alian.reservasikelas.service.repository

import id.alian.reservasikelas.service.ReservasiService
import id.alian.reservasikelas.service.model.BookRuangan
import javax.inject.Inject

class RuanganRepository @Inject constructor(val api: ReservasiService) {
    suspend fun getDataRuangan() = api.getDataRuangan()

    suspend fun getDataDosen() = api.getDataDosen()

    suspend fun getDataMataKuliah() = api.getDataMataKuliah()

    suspend fun bookRuangan(bookRuangan: BookRuangan) = api.bookRuangan(bookRuangan)
}