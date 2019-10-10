package com.chess.chessapi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.Serializable;

@Service
public class MailContentBuilderUtils implements Serializable {
    private TemplateEngine templateEngine;
    public static final String MAIL_INSTRUCTOR_APPROVED_TEMPLATE = "mailTemplate";
    public static final String SOURCE_LINK_GO_TO_PROFILE = "http://cols-capstone.ml/profile";
    public static final String SOURCE_NAME_GO_TO_PROFILE = "Đến Trang Cá Nhân";
    public static final String SOURCE_LINK_GO_TO_COURSE = "http://cols-capstone.ml/course/";
    public static final String SOURCE_NAME_GO_TO_COURSE = "Đến Trang Khóa Học";

    @Autowired
    public MailContentBuilderUtils(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(String username,String message,String source,String sourceName) {
        Context context = new Context();
        context.setVariable("name",username);
        context.setVariable("message", message);
        context.setVariable("source", source);
        context.setVariable("sourceName", sourceName);
        return templateEngine.process(MAIL_INSTRUCTOR_APPROVED_TEMPLATE, context);
    }
}
