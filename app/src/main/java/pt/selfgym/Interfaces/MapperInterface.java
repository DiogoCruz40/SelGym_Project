package pt.selfgym.Interfaces;


import java.util.List;

public interface MapperInterface {
    // S - Source and T - Target
    <S, T> T toEntity(S dto, Class<T> entityclass);

    <S, T> T toDTO(S entity, Class<T> dtoclass);

    <S, T> List<T> toEntities(List<S> dtos, Class<T> entityclass);

    <S, T> List<T> toDTOs(List<S> entitylist, Class<T> dtoclass);
}
