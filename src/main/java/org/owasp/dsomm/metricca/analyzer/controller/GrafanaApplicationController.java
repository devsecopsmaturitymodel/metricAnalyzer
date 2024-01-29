package org.owasp.dsomm.metricca.analyzer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.owasp.dsomm.metricca.analyzer.deserialization.Application;
import org.owasp.dsomm.metricca.analyzer.deserialization.ApplicationDirector;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
public class GrafanaApplicationController {
  private static final Logger logger = LoggerFactory.getLogger(GrafanaApplicationController.class);

  @Autowired
  private ApplicationDirector applicationDirector;

  @RequestMapping(value = "/applications", method = RequestMethod.GET)
  @ResponseBody
  public Collection<String> getApplicationIds() throws IOException, GitAPIException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    Set<String> applicationIds = new HashSet<>();
    for (Application application : applicationDirector.getApplications()) {
      applicationIds.add(application.getName());
    }
    return applicationIds;
  }

  @RequestMapping(value = "/activity-schema", method = RequestMethod.GET)
  @ResponseBody
  public String getApplicationSchema() throws IOException, GitAPIException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    ObjectMapper mapper = new ObjectMapper();
    SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
    mapper.acceptJsonFormatVisitor(Activity.class, visitor);
    JsonSchema jsonSchema = visitor.finalSchema();
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema);
  }
}