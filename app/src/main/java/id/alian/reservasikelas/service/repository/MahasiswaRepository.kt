package id.alian.reservasikelas.service.repository

import id.alian.reservasikelas.service.ReservasiService
import javax.inject.Inject

class MahasiswaRepository @Inject constructor(private val api: ReservasiService) {
    suspend fun loginMahasiswa(nim: String, password: String) = api.loginMahasiswa(nim, password)

    suspend fun getProfile(nim: String) = api.getProfileMahasiswa(nim)
}