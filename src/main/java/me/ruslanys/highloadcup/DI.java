package me.ruslanys.highloadcup;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.ruslanys.highloadcup.component.Config;
import me.ruslanys.highloadcup.dao.LocationDao;
import me.ruslanys.highloadcup.dao.UserDao;
import me.ruslanys.highloadcup.dao.VisitDao;
import me.ruslanys.highloadcup.dao.impl.LocationInMemoryDao;
import me.ruslanys.highloadcup.dao.impl.UserInMemoryDao;
import me.ruslanys.highloadcup.dao.impl.VisitInMemoryDao;
import me.ruslanys.highloadcup.service.LocationService;
import me.ruslanys.highloadcup.service.UserService;
import me.ruslanys.highloadcup.service.VisitService;
import me.ruslanys.highloadcup.service.impl.DefaultLocationService;
import me.ruslanys.highloadcup.service.impl.DefaultUserService;
import me.ruslanys.highloadcup.service.impl.DefaultVisitService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
public class DI {

    private static final Map<Class, Object> BEANS = new ConcurrentHashMap<>();

    static {
        // config
        BEANS.put(Config.class, new Config());

        // dao
        BEANS.put(UserDao.class, new UserInMemoryDao());
        BEANS.put(LocationDao.class, new LocationInMemoryDao());
        BEANS.put(VisitDao.class, new VisitInMemoryDao());

        // services
        BEANS.put(LocationService.class, new DefaultLocationService());
        BEANS.put(UserService.class, new DefaultUserService());
        BEANS.put(VisitService.class, new DefaultVisitService());

        // --
        BEANS.put(ObjectMapper.class, initMapper());
    }

    private static ObjectMapper initMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    public static void add(Class clazz, Object object) {
        BEANS.put(clazz, object);
    }

    public static <T> T getBean(Class<T> clazz) {
        Object value = BEANS.get(clazz);
        if (value == null) {
            throw new IllegalStateException("Bean " + clazz.getName() + " not found!");
        }
        return (T) value;
    }

    public static Config getConfig() {
        return getBean(Config.class);
    }

}
