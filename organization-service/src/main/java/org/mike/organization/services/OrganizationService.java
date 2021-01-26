package org.mike.organization.services;

import org.mike.organization.model.Organization;
import org.mike.organization.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository orgRepository;

    public Organization getOrg(String organizationId) {
        return orgRepository.findById(organizationId).orElse(null);
    }

    public void saveOrg(Organization org) {
        org.setId( UUID.randomUUID().toString());
        orgRepository.save(org);
    }

    public void updateOrg(Organization org) {
        orgRepository.save(org);
    }

    public void deleteOrg(Organization org) {
        orgRepository.delete(org);
    }

    public void deleteOrgById(String id) {
        orgRepository.deleteById(id);
    }
}
