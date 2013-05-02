package com.fidelis.k2.experimental.crmconnector;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("springApplicationContext.xml");
        CrmClient crmClient = (CrmClient) applicationContext.getBean("crmClient", CrmClient.class);
        crmClient.connect();

    }
}
