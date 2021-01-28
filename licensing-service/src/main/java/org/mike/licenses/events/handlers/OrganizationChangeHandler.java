package org.mike.licenses.events.handlers;

import org.mike.licenses.clients.OrganizationRestTemplateClient;
import org.mike.licenses.events.CustomChannels;
import org.mike.licenses.events.models.OrganizationChangeModel;
import org.mike.licenses.repository.OrganizationRedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

//@EnableBinding(Sink.class)
@EnableBinding(CustomChannels.class)
public class OrganizationChangeHandler {

    private static final Logger log = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

    @Autowired
    private OrganizationRedisRepository orgRepo;

    //@StreamListener(Sink.INPUT)
    @StreamListener("inboundOrgChanges")
    public void loggerSink(OrganizationChangeModel orgChange) {
        log.info("### RECEIVED KAFKA MESSAGE - AN EVENT for organization id {}", orgChange.getOrganizationId());
        orgRepo.deleteById(orgChange.getOrganizationId());
    }
}
