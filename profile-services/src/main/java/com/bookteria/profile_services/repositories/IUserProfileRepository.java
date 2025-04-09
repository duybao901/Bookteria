package com.bookteria.profile_services.repositories;

import com.bookteria.profile_services.entities.UserProfile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserProfileRepository extends Neo4jRepository<UserProfile, String> {
}
