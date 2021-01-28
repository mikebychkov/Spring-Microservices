package org.mike.licenses.clients;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mike.licenses.model.Organization;
import org.mike.licenses.repository.OrganizationRedisRepository;
import org.mike.licenses.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationRestTemplateClient {

    private static final Logger log = LogManager.getLogger(OrganizationRestTemplateClient.class);

    @Autowired
    //OAuth2RestTemplate restTemplate;
    private RestTemplate restTemplate;

    @Autowired
    private OrganizationRedisRepository orgRepo;

    private Organization checkRedisCache(String organizationId) {
        try {
            return orgRepo.findById(organizationId).orElse(null);
        } catch (Exception ex) {
            log.error("### Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}", organizationId, ex);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization org) {
        try {
            orgRepo.save(org);
        } catch (Exception ex) {
            log.error("### Unable to cache organization {} in Redis. Exception {}", org.getId(), ex);
        }
    }

    public Organization getOrganization(String organizationId){

        Organization rsl = checkRedisCache(organizationId);
        if (rsl != null) {
            log.info("### I have successfully retrieved an organization {} FROM REDIS CACHE: {}", organizationId, rsl);
            return rsl;
        }

        log.info("### In Licensing Service.getOrganization: {}", UserContextHolder.getContext().getCorrelationId());

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(
                        "http://zuulservice:5555/api/organization/v1/organizations/{organizationId}",
                        HttpMethod.GET,
                        null, Organization.class, organizationId);

        rsl = restExchange.getBody();

        cacheOrganizationObject(rsl);

        return rsl;
    }
}
