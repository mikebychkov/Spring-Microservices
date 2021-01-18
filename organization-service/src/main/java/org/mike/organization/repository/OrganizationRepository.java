package org.mike.organization.repository;

import org.mike.organization.model.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization,String> {
}
