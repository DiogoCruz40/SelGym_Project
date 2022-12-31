package pt.selfgym.mappers;


import pt.selfgym.Interfaces.MapperInterface;
import pt.selfgym.utils.MapperUtil;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;


public class Mapper implements MapperInterface {

    ModelMapper modelMapper;

    public Mapper() {
        modelMapper = MapperUtil.getMapper();
    }

    private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    @Override
    public <S, T> T toEntity(S dto, Class<T> entityclass) {
        return modelMapper.map(dto, entityclass);
    }

    @Override
    public <S, T> T toDTO(S entity, Class<T> dtoclass) {
        return modelMapper.map(entity, dtoclass);
    }

    @Override
    public <S, T> List<T> toEntities(List<S> dtos, Class<T> entityclass) {
        return mapList(dtos, entityclass);
    }

    @Override
    public <S, T> List<T> toDTOs(List<S> entitylist, Class<T> dtoclass) {
        return mapList(entitylist, dtoclass);
    }

}
