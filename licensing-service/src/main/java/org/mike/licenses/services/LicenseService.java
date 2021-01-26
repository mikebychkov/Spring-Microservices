package org.mike.licenses.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mike.licenses.clients.OrganizationRestTemplateClient;
import org.mike.licenses.model.License;
import org.mike.licenses.model.Organization;
import org.mike.licenses.repository.LicenseRepository;
import org.mike.licenses.utils.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {

    private static final Logger log = LogManager.getLogger(LicenseService.class);

    @Autowired
    private LicenseRepository licenseRepository;

    private void randomlyRunLong() {
        Random rnd = new Random();
        int rsl = rnd.nextInt(3) + 1;
        if (rsl == 3) {
            try {
                Thread.sleep(110000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<License> buildFallbackLicenseList(String organizationId) {
        return List.of(
                new License().withId("000000000-00-00000")
                .withOrganizationId(organizationId)
                .withProductName("Sorry, no licensing information currently available.")
        );
    }

    // COMMON PROPERTIES CAN BE DEFINED ON THE CLASS LEVEL WITH ANNOTATION @DefaultProperties
    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
        threadPoolKey = "getLicensesByOrgThreadPool",
        threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "30"),
            @HystrixProperty(name = "maxQueueSize", value = "10")
        },
        commandProperties = {
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
            @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
        }
    )
    public List<License> getLicensesByOrg(String organizationId) {
        //randomlyRunLong(); // FOR IMAGE CIRCUIT BREAKER WORK SAKE

        log.info("### CORRELATION ID: {}", UserContextHolder.getContext().getCorrelationId());

        return licenseRepository.findByOrganizationId(organizationId);
    }

    @HystrixCommand
    public void saveLicense(License license) {
        license.withId(UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    @HystrixCommand
    public void updateLicense(License license) {
        licenseRepository.save(license);
    }

    @HystrixCommand
    public void deleteLicense(License license) {
        licenseRepository.deleteById(license.getLicenseId());
    }


    // DISCOVERY

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @HystrixCommand
    private Organization getOrganization(String organizationId) {
        return organizationRestClient.getOrganization(organizationId);
    }

    @HystrixCommand
    public License getLicense(String organizationId, String licenseId) {

        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = getOrganization(organizationId);

        return license
                .withOrganizationName(org.getName())
                .withContactName(org.getContactName())
                .withContactEmail(org.getContactEmail())
                .withContactPhone(org.getContactPhone());
    }
}
