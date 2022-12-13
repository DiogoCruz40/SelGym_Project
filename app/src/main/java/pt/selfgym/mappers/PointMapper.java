package pt.selfgym.mappers;


import pt.selfgym.Interfaces.PointMapperInterface;
import pt.selfgym.database.entities.Point;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;



public class PointMapper implements PointMapperInterface {

    ModelMapper modelMapper;

    public PointMapper() {
        modelMapper = MapperUtil.getMapper();
    }

    private <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    /*@Override
    public Point toEntityPoint(PointDTO pointDTO) {
        return modelMapper.map(pointDTO, Point.class);
    }

    @Override
    public PointDTO toPointDTO(Point point) {
        return modelMapper.map(point, PointDTO.class);
    }

    @Override
    public List<Point> toEntityPoints(List<PointDTO> pointsDTO) {
        return mapList(pointsDTO, Point.class);
    }

    @Override
    public List<PointDTO> toPointsDTO(List<Point> points) {
        return mapList(points, PointDTO.class);
    }*/

}
