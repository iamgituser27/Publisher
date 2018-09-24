package com.hcl.ccp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.util.concurrent.ListenableFutureCallback;

//@EnableAutoConfiguration
@SpringBootApplication
public class PubSubApplication {

  private static final Log LOGGER = LogFactory.getLog(PubSubApplication.class);

  public String TOPICS = "hybrid-cloud";
  public String SUBS = "hybrid-cloud-subs";


  public static void main(String[] args) {
    SpringApplication.run(PubSubApplication.class, args);
  }

  @Bean
  @ServiceActivator(inputChannel = "pubSubOutputChannel")
  public MessageHandler messageSender(PubSubTemplate pubsubTemplate) {

    PubSubMessageHandler adapter =
            new PubSubMessageHandler(pubsubTemplate, TOPICS);
    adapter.setPublishCallback(new ListenableFutureCallback<String>() {
    @Override
    public void onFailure(Throwable ex) {
        LOGGER.info("There was an error sending the message.");

     }

      @Override
      public void onSuccess(String result) {
        LOGGER.info("Message was sent successfully.");

      }
    });

    return adapter;
  }

  @MessagingGateway(defaultRequestChannel = "pubSubOutputChannel")
  public interface PubSubOutboundGateway {
    String sendToPubSub(String message);
  }
}