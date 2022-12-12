package pt.selfgym.Interfaces;

import pt.selfgym.database.entities.Point;
import pt.selfgym.dtos.PointDTO;

import java.util.List;


public interface PointMapperInterface {
     Point toEntityPoint(PointDTO pointDTO);
     PointDTO toPointDTO(Point point);
     List<Point> toEntityPoints(List<PointDTO> pointDTO);
     List<PointDTO> toPointsDTO(List<Point> points);
}
