package an.imation.filmsapp.domain.model

sealed class MovieExceptionDomainModel(exception: Throwable) : Throwable(exception) {
    override val cause: Throwable = exception

    class NoInternetConnection(exception: Throwable) : MovieExceptionDomainModel(exception)
    class Other(exception: Throwable) : MovieExceptionDomainModel(exception)

}
