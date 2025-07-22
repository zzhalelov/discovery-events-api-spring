package kz.zzhalelov.server.request;

import java.util.List;

public interface RequestService {
    Request create(Request request, long userId, long eventId);

//    List<Request> findRequestsByUser(Request request, Long userID);
//
//    Request update(Request request, Long userId);
}
