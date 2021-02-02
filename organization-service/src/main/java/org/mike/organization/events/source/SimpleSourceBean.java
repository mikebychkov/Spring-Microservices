package org.mike.organization.events.source;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mike.organization.events.models.OrganizationChangeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleSourceBean {

    private Source source;

    private static final Logger log = LogManager.getLogger(SimpleSourceBean.class);

    @Autowired
    public SimpleSourceBean(Source source) {
        this.source = source;
    }

    public void publishOrgChange(String action, String orgId) {
        log.info("### SENDING KAFKA MESSAGE: {} for Organization Id: {}", action, orgId);
        OrganizationChangeModel change =  new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action,
                orgId,
                ""
        );

        source.output().send(MessageBuilder.withPayload(change).build());
    }
}
