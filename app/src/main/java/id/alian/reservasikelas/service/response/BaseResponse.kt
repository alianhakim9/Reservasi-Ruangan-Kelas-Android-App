package id.alian.reservasikelas.service.response

data class BaseResponse<T>(
    val message: String,
    val data: T,
)
