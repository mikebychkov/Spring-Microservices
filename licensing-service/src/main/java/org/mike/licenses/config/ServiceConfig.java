package org.mike.licenses.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {

  @Value("${example.property}")
  private String exampleProperty;

  @Value("${spring.datasource.password}")
  private String dbPassword;

  public String getExampleProperty(){
    return exampleProperty;
  }

  public String getDbPassword() {
    return dbPassword;
  }
}
