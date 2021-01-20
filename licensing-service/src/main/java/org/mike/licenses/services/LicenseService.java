package org.mike.licenses.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.mike.licenses.clients.OrganizationDiscoveryClient;
import org.mike.licenses.clients.OrganizationFeignClient;
import org.mike.licenses.clients.OrganizationRestTemplateClient;
import org.mike.licenses.model.License;
import org.mike.licenses.model.Organization;
import org.mike.licenses.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {

    @Autowired
    private LicenseRepository licenseRepository;

    @HystrixCommand
    public License getLicense(String organizationId, String licenseId) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        return license;
    }

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

    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList",
        threadPoolKey = "getLicensesByOrgThreadPool",
        threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "30"),
            @HystrixProperty(name = "maxQueueSize", value = "10")
        }
    )
    public List<License> getLicensesByOrg(String organizationId) {
        randomlyRunLong(); // FOR IMAGE CIRCUIT BREAKER WORK SAKE
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
    OrganizationFeignClient organizationFeignClient;

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;

    @Autowired
    OrganizationDiscoveryClient organizationDiscoveryClient;

    private Organization retrieveOrgInfo(String organizationId, String clientType) {
        Organization organization = null;

        switch (clientType) {
            case "feign":
                System.out.println("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organization = organizationRestClient.getOrganization(organizationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
                break;
            default:
                organization = organizationRestClient.getOrganization(organizationId);
        }

        return organization;
    }

    @HystrixCommand
    public License getLicense(String organizationId,String licenseId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

        Organization org = retrieveOrgInfo(organizationId, clientType);

        return license
                .withOrganizationName(org.getName())
                .withContactName(org.getContactName())
                .withContactEmail(org.getContactEmail())
                .withContactPhone(org.getContactPhone());
    }
}
