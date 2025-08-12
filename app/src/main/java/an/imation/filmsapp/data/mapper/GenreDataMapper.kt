package an.imation.filmsapp.data.mapper

import an.imation.filmsapp.data.model.GenreApiModel
import an.imation.filmsapp.domain.model.GenreDomainModel

class GenreDataMapper {
    fun toDomain(apiModel: GenreApiModel) = runCatching {
        GenreDomainModel(
            id = apiModel.id?.toInt()!!,
            name = apiModel.name!!
        )
    }.getOrElse {
        null
    }
}