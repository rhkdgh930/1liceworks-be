package com.elice.iliceworksbe.notification.repository.mongo;


import com.elice.iliceworksbe.notification.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    // 특정 사용자에 대한 최근 1개월 이내의 최신 알림 최대 50개 조회
    @Query(value = "{ 'userId': ?0, 'notifyTime': { $gte: ?1 } }", sort = "{ 'createdAt': -1 }")
    List<Notification> findTop50ByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(String userId, LocalDateTime createdAt);

}
