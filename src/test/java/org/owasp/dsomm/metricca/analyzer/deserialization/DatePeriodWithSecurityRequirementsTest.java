package org.owasp.dsomm.metricca.analyzer.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.owasp.dsomm.metricca.analyzer.ApplicationTest;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.UrlActivity;
import org.owasp.dsomm.metricca.analyzer.deserialization.activity.threshold.DatePeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

public class DatePeriodWithSecurityRequirementsTest extends ApplicationTest {
  private static final Logger logger = LoggerFactory.getLogger(DatePeriodWithSecurityRequirementsTest.class);

  private UrlActivity getSecurityRequirementsActivity() throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
    List<Application> actualApplications = applicationDirector.getApplications();
    for (Application application : actualApplications) {
      if (application.getName().equals("sauron")) {
        return (UrlActivity) application.getActivities().get(0);
      }
    }
    return null;
  }

  private DatePeriod getMatchingDatePeriodComponent(String givenDateString) throws GitAPIException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParseException {
    UrlActivity activity = getSecurityRequirementsActivity();
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
    isoFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
    Date givenDate = isoFormat.parse(givenDateString);
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

//    logger.info("DatePeriodMap: " + objectMapper.writeValueAsString(activity.getThresholdDatePeriodMap()));

    return activity.getThresholdDatePeriodMap().get("Level 1").getDatePeriodForDate(givenDate);
  }

  @Test
  public void testDateNotExists() throws Exception {
    DatePeriod datePeriod = getMatchingDatePeriodComponent("1980-01-01");
    assertNull(datePeriod);
  }

  @Test
  public void testShowEndDateWithOtherDatesInPeriod() throws Exception {
    DatePeriod startOfDatePeriod = getMatchingDatePeriodComponent("2022-05-01");
    logger.info("Start of date period: " + startOfDatePeriod.getDate() + " " + startOfDatePeriod.getEndDate() + " " + startOfDatePeriod.getShowEndDate());
    assertFalse(startOfDatePeriod.getShowEndDate());

    DatePeriod middleDatePeriod = getMatchingDatePeriodComponent("2023-01-01");
    assertFalse(middleDatePeriod.getShowEndDate());

    DatePeriod lastDatePeriod = getMatchingDatePeriodComponent("2023-12-19");
    assertTrue(lastDatePeriod.getShowEndDate());
  }

  @Test
  public void testShowEndDateWithEndDateToShowForSingleDate() throws Exception {
    UrlActivity activity = getSecurityRequirementsActivity();
    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
    isoFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));

    Date existingDate = isoFormat.parse("2023-12-19");
    DatePeriod endDateForExistingDate = activity.getThresholdDatePeriodMap().get("Level 1").getDatePeriodForDate(existingDate);
    assertNotNull(endDateForExistingDate);
    assertTrue(endDateForExistingDate.getShowEndDate());
  }
}
