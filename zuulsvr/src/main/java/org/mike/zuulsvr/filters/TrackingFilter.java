package org.mike.zuulsvr.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrackingFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;
    private static final Logger log = LogManager.getLogger(TrackingFilter.class);

//    @Autowired
//    FilterUtils filterUtils;

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private boolean isCorrelationIdPresent() {
//      if (filterUtils.getCorrelationId() !=null) {
//          return true;
//      }
      return false;
    }

    private String generateCorrelationId(){
        return java.util.UUID.randomUUID().toString();
    }

    public Object run() {

//        if (isCorrelationIdPresent()) {
//           log.info("### tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
//        }
//        else{
//            filterUtils.setCorrelationId(generateCorrelationId());
//            log.info("### tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
//        }

        RequestContext ctx = RequestContext.getCurrentContext();
        log.info("### Processing incoming request for {}.",  ctx.getRequest().getRequestURI());

        return null;
    }
}