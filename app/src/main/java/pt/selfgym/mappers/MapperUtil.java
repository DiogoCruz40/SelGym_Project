package pt.selfgym.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;


public class MapperUtil {
    private static ModelMapper MAPPER;

    static ModelMapper getMapper() {
        if (MAPPER == null) {
            MAPPER = new ModelMapper();
            MAPPER.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
            // http://modelmapper.org/user-manual/configuration/
        }
        return MAPPER;
    }

}
