package kr.pe.randy.showmethebusstop.data.mapper

interface Mapper<E, D>  {
    fun mapFromEntity(domainType: E): D
    fun mapToEntity(dataType: D): E
}