package kz.zhelezyaka.dto.mapper;

public interface Mapper<E, T> {
    E toEntity(T dto);

    T toDto(E user);
}
