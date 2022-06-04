package id.alian.reservasikelas.dependecy_injection

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.alian.reservasikelas.service.ReservasiService
import id.alian.reservasikelas.service.repository.DosenRepository
import id.alian.reservasikelas.service.repository.MahasiswaRepository
import id.alian.reservasikelas.service.repository.RuanganRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideReservasiService(): ReservasiService {

        val client = OkHttpClient.Builder()
            .callTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://reservasiapi.pagekite.me/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ReservasiService::class.java)
    }

    @Provides
    fun provideMahasiswaRepository(api: ReservasiService): MahasiswaRepository {
        return MahasiswaRepository(api)
    }

    @Provides
    fun providesDosenRepository(api: ReservasiService): DosenRepository {
        return DosenRepository(api)
    }

    @Provides
    fun providesRuanganRepository(api: ReservasiService): RuanganRepository {
        return RuanganRepository(api)
    }
}