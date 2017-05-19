package com.test.sedaqueue;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SedaQueueApplication implements CommandLineRunner {

	@Autowired
	@EndpointInject(uri = "direct://direct-queue")
	ProducerTemplate producerTemplate;

	public static void main(String[] args) {
		SpringApplication.run(SedaQueueApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		producerTemplate.sendBody("Teste Direct - Async");
	}

	@Component
	class Router extends RouteBuilder {

		@Override
		public void configure() throws Exception {
			from("direct://direct-queue").routeId("toAsync").to("seda://async-queue?size=100");

			from("seda://async-queue").routeId("toLog").log("${body}");
		}
	}
}
