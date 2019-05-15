package info.alaz.stock.manager.mapper;

import java.util.List;
import java.util.Set;

public interface GenericEntityDtoMapper<D, E> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntityList(List<D> dtoList);

    List<D> toDtoList(List<E> entityList);

    Set<E> toEntitySet(Set<D> dtoList);

    Set<D> toDtoSet(Set<E> entityList);
}