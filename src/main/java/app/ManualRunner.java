package app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ManualRunner {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("app.config");
        ctx.getEnvironment().setActiveProfiles("dev");
        ctx.refresh();

    }

}
