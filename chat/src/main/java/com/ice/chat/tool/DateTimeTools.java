package com.ice.chat.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeTools {

    @Tool(description = "获取用户在指定时区的当前日期和时间，用于回答需要实时日期和时间的问题")
    public String getCurrentTime() {
        var zoneId = LocaleContextHolder.getTimeZone().toZoneId();
        var now = LocalDateTime.now().atZone(zoneId);
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);
    }

    @Tool(description = "设置闹钟，调用此工具可以在指定时间触发提醒。时间参数必是 ISO-8601 格式。例如： 2026-06-18 15:51:22")
    public void setAlarm(String time) {
        System.out.println("闹钟已设置在：" + time);
    }
}
