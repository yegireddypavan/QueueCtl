package com.example.queuectl.util;

import org.springframework.context.ApplicationContext;
import picocli.CommandLine.IFactory;

public class SpringFactory implements IFactory {
    private final ApplicationContext ctx;

    public SpringFactory(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public <K> K create(Class<K> cls) throws Exception {
        return ctx.getBean(cls);
    }
}
