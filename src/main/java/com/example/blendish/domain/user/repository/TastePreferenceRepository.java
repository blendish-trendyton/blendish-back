package com.example.blendish.domain.user.repository;

import com.example.blendish.domain.user.entity.TastePreference;
import com.example.blendish.domain.user.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TastePreferenceRepository extends JpaRepository<TastePreference,Long> {
    @Transactional
    void deleteByUser(User user);

}
