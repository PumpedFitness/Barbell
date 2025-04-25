package ord.pumped.common.mapping

fun interface IModelMapper<Model, DTO> {

    fun toDomain(dto: DTO): Model

}