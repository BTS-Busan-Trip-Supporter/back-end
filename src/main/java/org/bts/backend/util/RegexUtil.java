package org.bts.backend.util;

import java.time.LocalTime;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bts.backend.domain.constant.ContentType;
import org.bts.backend.dto.response.tourapi.DetailIntroResponse;

public class RegexUtil {

    public static LocalTime[] extractOpeningHours(
        String contentTypeId,
        DetailIntroResponse response
    ) {
        String openingHoursText = getOpeningHoursText(contentTypeId, response);

        if (openingHoursText == null) {
            return new LocalTime[0];
        }

        String pattern = "(\\d{1,2}:\\d{2})\\s*[-~]\\s*(?:[^\\d]*)(\\d{1,2}:\\d{2})";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(openingHoursText);

        // 매칭된 결과 출력
        if (matcher.find()) {
            String openTime = matcher.group(1);  // 오픈 시간
            String closeTime = matcher.group(2); // 마감 시간

            StringTokenizer openTimeSt = new StringTokenizer(openTime, ":");
            StringTokenizer closeTimeSt = new StringTokenizer(closeTime, ":");

            return new LocalTime[] {
                LocalTime.of(
                    Math.min(23, Integer.parseInt(openTimeSt.nextToken())),
                    Integer.parseInt(openTimeSt.nextToken())
                ),
                LocalTime.of(
                    Math.min(23, Integer.parseInt(closeTimeSt.nextToken())),
                    Integer.parseInt(closeTimeSt.nextToken())
                )
            };
        }

        return new LocalTime[0];
    }

    private static String getOpeningHoursText(
        String contentTypeId,
        DetailIntroResponse response
    ) {
        ContentType contentType = ContentType.of(Integer.parseInt(contentTypeId));
        return response.getOpeningTime(contentType);
    }

    public static String extractHomepageURL(String homepageText) {
        String pattern = "href=\\\"(.*?)\\\"";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(homepageText);

        // 매칭된 결과가 있는지 확인
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
