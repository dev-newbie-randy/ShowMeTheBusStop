package kr.pe.randy.showmethebusstop.data.mapper

interface ListMapper<E, D> {
    fun mapFromEntityList(domainType: List<E>): List<D>
    fun mapToEntityList(dataType: List<D>): List<E>
}