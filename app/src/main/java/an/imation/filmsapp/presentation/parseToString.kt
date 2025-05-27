package an.imation.filmsapp.presentation

import an.imation.filmsapp.R
import an.imation.filmsapp.domain.MovieExceptionDomainModel

fun MovieExceptionDomainModel.parseToString() = when (this) {
    is MovieExceptionDomainModel.NoInternetConnection -> R.string.error_no_internet
    is MovieExceptionDomainModel.Other ->  R.string.error_loading
}