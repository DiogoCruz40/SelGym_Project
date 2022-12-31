package pt.selfgym.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;


public class MapperUtil {
    private static ModelMapper MAPPER;

    public static ModelMapper getMapper() {
        if (MAPPER == null) {
            MAPPER = new ModelMapper();
            MAPPER.getConfiguration().setFieldMatchingEnabled(true).setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
            // http://modelmapper.org/user-manual/configuration/
        }
        return MAPPER;
    }

}
