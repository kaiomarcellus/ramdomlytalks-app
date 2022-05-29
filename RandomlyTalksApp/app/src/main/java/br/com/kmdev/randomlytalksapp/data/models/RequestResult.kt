package br.com.kmdev.randomlytalksapp.data.models

data class RequestResult<out T>(
    val status: Status,
    val data: T?,
    val errorMessage: String?,
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): RequestResult<T> {
            return RequestResult(Status.SUCCESS, data, null)
        }

        fun <T> error(errorMessage: String? = null): RequestResult<T> {
            return RequestResult(Status.ERROR, null, errorMessage)
        }

        fun <T> loading(data: T? = null): RequestResult<T> {
            return RequestResult(Status.LOADING, data, null)
        }
    }

    fun isLoading() = this.status == Status.LOADING
    fun isSuccess() = this.status == Status.SUCCESS
    fun isError() = this.status == Status.ERROR

}