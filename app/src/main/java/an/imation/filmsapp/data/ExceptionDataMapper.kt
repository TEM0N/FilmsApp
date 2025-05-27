package an.imation.filmsapp.data

import an.imation.filmsapp.domain.MovieExceptionDomainModel
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

 fun Throwable.toMovieExceptionDomainModel(): MovieExceptionDomainModel {
    return when {
        this is SocketTimeoutException -> MovieExceptionDomainModel.NoInternetConnection(this)
        this is ConnectException -> MovieExceptionDomainModel.NoInternetConnection(this)
        this is UnknownHostException -> MovieExceptionDomainModel.NoInternetConnection(this)
        this is java.net.SocketException && this.message?.contains("Network is unreachable") == true ->
            MovieExceptionDomainModel.NoInternetConnection(this)
        this is MovieExceptionDomainModel -> this
        else -> MovieExceptionDomainModel.Other(this)
    }
}