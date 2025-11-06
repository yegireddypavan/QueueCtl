package com.example.queuectl;

import com.example.queuectl.cli.QueueCtlRootCommand;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import picocli.CommandLine;

import com.example.queuectl.util.SpringFactory;
import picocli.CommandLine;

import java.time.LocalDateTime;
import java.time.ZoneId;

@SpringBootApplication
public class QueueCtlApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = new SpringApplicationBuilder(QueueCtlApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		System.out.println("Java system timezone: " + ZoneId.systemDefault());
		System.out.println("LocalDateTime.now(): " + LocalDateTime.now());
		System.out.println("JDBC TimeZone = " + ZoneId.systemDefault());


		int exit = new CommandLine(
				ctx.getBean(QueueCtlRootCommand.class),
				new SpringFactory(ctx)   // ✅ Custom factory that works in all setups
		).execute(args);

		System.exit(exit);
	}
}

