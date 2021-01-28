package org.mike.licenses.repository;

import org.mike.licenses.model.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRedisRepository extends CrudRepository<Organization, String> {
}
