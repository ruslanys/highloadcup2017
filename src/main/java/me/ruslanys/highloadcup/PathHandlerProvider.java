
package me.ruslanys.highloadcup;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import me.ruslanys.highloadcup.handler.*;

public class PathHandlerProvider {

    private static final UserGetHandler USER_GET_HANDLER = new UserGetHandler();
    private static final UserAddHandler USER_ADD_HANDLER = new UserAddHandler();
    private static final UserUpdateHandler USER_UPDATE_HANDLER = new UserUpdateHandler();
    private static final UserVisitsGetHandler USER_VISITS_GET_HANDLER = new UserVisitsGetHandler();

    private static final LocationGetHandler LOCATION_GET_HANDLER = new LocationGetHandler();
    private static final LocationAddHandler LOCATION_ADD_HANDLER = new LocationAddHandler();
    private static final LocationUpdateHandler LOCATION_UPDATE_HANDLER = new LocationUpdateHandler();
    private static final LocationAvgGetHandler LOCATION_AVG_GET_HANDLER = new LocationAvgGetHandler();

    private static final VisitGetHandler VISIT_GET_HANDLER = new VisitGetHandler();
    private static final VisitAddHandler VISIT_ADD_HANDLER = new VisitAddHandler();
    private static final VisitUpdateHandler VISIT_UPDATE_HANDLER = new VisitUpdateHandler();


    @Deprecated
    public static HttpHandler getHandler(FullHttpRequest request) {
        String uri = request.uri();
        if (uri.contains("?")) {
            uri = uri.substring(0, uri.indexOf("?"));
        }

        HttpMethod method = request.method();
        if (method.equals(HttpMethod.POST)) {
            if (uri.startsWith("/users")) {
                if (uri.endsWith("/new")) {
                    return USER_ADD_HANDLER;
                } else {
                    return USER_UPDATE_HANDLER;
                }
            }

            if (uri.startsWith("/locations")) {
                if (uri.endsWith("/new")) {
                    return LOCATION_ADD_HANDLER;
                } else {
                    return LOCATION_UPDATE_HANDLER;
                }
            }

            if (uri.startsWith("/visits")) {
                if (uri.endsWith("/new")) {
                    return VISIT_ADD_HANDLER;
                } else {
                    return VISIT_UPDATE_HANDLER;
                }
            }
        } else if (method.equals(HttpMethod.GET)) {
            if (uri.endsWith("/avg")) {
                return LOCATION_AVG_GET_HANDLER;
            } else if (uri.endsWith("/visits")) {
                return USER_VISITS_GET_HANDLER;
            }

            if (uri.startsWith("/users")) {
                return USER_GET_HANDLER;
            }

            if (uri.startsWith("/locations")) {
                return LOCATION_GET_HANDLER;
            }

            if (uri.startsWith("/visits")) {
                return VISIT_GET_HANDLER;
            }
        }

        return null;
    }

}
