package org.mike.authenticationservice.repository;

import org.mike.authenticationservice.model.UserOrganization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgUserRepository extends CrudRepository<UserOrganization, String> {
    public UserOrganization findByUserName(String userName);
}
