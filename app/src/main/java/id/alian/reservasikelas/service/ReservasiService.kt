package id.alian.reservasikelas.service

import id.alian.reservasikelas.service.model.*
import id.alian.reservasikelas.service.response.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ReservasiService {

    @POST("login/mahasiswa")
    @FormUrlEncoded
    suspend fun loginMahasiswa(
        @Field("nim") nim: String,
        @Field("password") password: String,
    ): Response<BaseResponse<Mahasiswa>>

    @POST("login/dosen")
    @FormUrlEncoded
    suspend fun loginDosen(
        @Field("nidn") nidn: String,
        @Field("password") password: String,
    ): Response<BaseResponse<Dosen>>

    @GET("mahasiswa/profil/{nim}")
    suspend fun getProfileMahasiswa(
        @Path("nim") nim: String,
    ): Response<BaseResponse<List<Mahasiswa>>>

    @GET("dosen/profil/{nidn}")
    suspend fun getProfileDosen(
        @Path("nidn") nidn: String,
    ): Response<BaseResponse<Dosen>>

    @GET("ruangan/all")
    suspend fun getDataRuangan(): Response<BaseResponse<List<Ruangan>>>

    @GET("dosen/all")
    suspend fun getDataDosen(): Response<BaseResponse<List<Dosen>>>

    @GET("matakuliah/all")
    suspend fun getDataMataKuliah(): Response<BaseResponse<List<MataKuliah>>>

    @POST("bookruangan/book")
    suspend fun bookRuangan(
        @Body bookRuangan: BookRuangan,
    ): Response<BaseResponse<BookRuangan>>

    @GET("bookruangan/status/{id_mahasiswa}")
    suspend fun getStatusRuangan(
        @Path("id_mahasiswa") idMahasiswa: Int,
    ): Response<BaseResponse<List<BookRuangan>>>

    @PUT("mahasiswa/profil/update/{id_mahasiswa}")
    suspend fun editProfileMhs(
        @Path("id_mahasiswa") idMahasiswa: Int,
        @Query("email") email: String,
        @Query("password") password: String,
    ): Response<BaseResponse<Mahasiswa>>
}