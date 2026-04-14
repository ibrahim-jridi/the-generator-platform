package com.pfe.repository;

import com.pfe.domain.ActivityType;
import com.pfe.domain.User;
import java.util.List;
import java.util.UUID;

public interface ActivityTypeRepository extends BaseRepository<ActivityType, UUID> {

  List<ActivityType> findByUserAndDeletedFalse(User user);
}
