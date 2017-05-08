package com.creants.muext;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.creants.creants_2x.QAntServer;
import com.creants.creants_2x.core.util.AppConfig;

/**
 * @author LamHM
 *
 */
@SpringBootApplication
public class Creants2XApplication {
	private static ConfigurableApplicationContext context;


	public static <T> T getBean(Class<T> aClass) {
		return context.getBean(aClass);
	}


	public static void main(String[] args) throws InterruptedException {
		System.setProperty("log4j.configurationFile", "config/log4j2.xml");
		PropertyConfigurator.configure("config/log4j.properties");
		AppConfig.init("config/application.properties");

		context = SpringApplication.run(Creants2XApplication.class, args);
		QAntServer.getInstance().start();
	}
}
