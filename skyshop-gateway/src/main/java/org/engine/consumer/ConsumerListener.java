package org.engine.consumer;

import org.engine.plugin.transactions.factory.SaleRequestFactory;
import org.engine.plugin.transactions.factory.SaleResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ConsumerListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerListener.class);

	private static String topic = "tp-sale";

	@KafkaListener(topics = "tp-sale")
	public SaleResponseFactory transactionElavonAuthorizeProcess(@Payload SaleRequestFactory tf, @Headers MessageHeaders headers) throws Exception {

		System.out.println(tf.getId());

		SaleResponseFactory resObj = new SaleResponseFactory();
		resObj.setUnique_id("123123");

		return resObj;
	}
}
