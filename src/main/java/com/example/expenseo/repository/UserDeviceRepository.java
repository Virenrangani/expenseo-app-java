package com.example.expenseo.repository;

import com.example.expenseo.models.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    Optional<UserDevice> findByFcmToken(String fcmToken);

    List<UserDevice> findAllByUserIdIn(Collection<String> userIds);

    @Modifying
    @Query("DELETE FROM UserDevice u WHERE u.fcmToken = :fcmToken")
    void deleteByFcmToken(@Param("fcmToken") String fcmToken);

    @Modifying
    @Query("DELETE FROM UserDevice u WHERE u.fcmToken IN :tokens")
    void deleteAllByFcmTokenIn(@Param("tokens") Collection<String> tokens);
}