package com.techietag.springaiagent.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

/**
 * Utility tool that returns the current date/time for a given IANA time zone identifier.
 *
 * <p>This class is exposed as a Spring-managed service and declares a tool method used by
 * Spring AI tooling. The method expects a valid IANA time zone ID (for example
 * "Europe/London", "America/New_York", or "Asia/Kolkata") and returns the current
 * zoned date-time as a string. If an invalid or unknown time zone identifier is
 * provided, the method returns a user-friendly error message instead of throwing an exception.
 *
 * <p>Usage example:
 * <pre>
 * GetCurrentTime tool = applicationContext.getBean(GetCurrentTime.class);
 * String nowInLondon = tool.getCurrentTime("Europe/London");
 * </pre>
 * <p>
 * See: java.time.ZoneId for valid IANA zone identifiers and java.time.ZonedDateTime for
 * formatting options if you need a different output format.
 */
@Service
public class GetCurrentTime {

    private static final Logger log = LoggerFactory.getLogger(GetCurrentTime.class);

    /**
     * Returns the current date and time for the specified IANA time zone identifier.
     */
    @Tool(
            description = "Get the current time for a specified country",
            name = "GetCurrentTimeTool")
    public String getCurrentTime(String country) {

        log.info("GetCurrentTime tool invoked with country/region: {}", country);
        java.time.ZoneId zoneId;
        try {
            zoneId = java.time.ZoneId.of(country);
        } catch (Exception e) {
            return "Invalid country/region provided. Please provide a valid  time zone identifier.";
        }
        java.time.ZonedDateTime zonedDateTime = java.time.ZonedDateTime.now(zoneId);
        return zonedDateTime.toString();

    }
}
