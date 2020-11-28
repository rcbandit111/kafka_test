package org.engine.rest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.engine.plugin.transactions.factory.SaleRequestFactory;
import org.engine.plugin.transactions.factory.SaleResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutController.class);

    private KafkaTemplate<String, SaleRequestFactory> saleRequestFactoryKafkaTemplate;
    private ReplyingKafkaTemplate<String, SaleRequestFactory, SaleResponseFactory> requestReplyKafkaTemplate;

    @Autowired
    public CheckoutController(KafkaTemplate<String, SaleRequestFactory> saleRequestFactoryKafkaTemplate,
                              ReplyingKafkaTemplate<String, SaleRequestFactory, SaleResponseFactory> requestReplyKafkaTemplate){
        this.saleRequestFactoryKafkaTemplate = saleRequestFactoryKafkaTemplate;
        this.requestReplyKafkaTemplate = requestReplyKafkaTemplate;
    }

    @PostMapping("test")
    public void performPayment() throws ExecutionException, InterruptedException, TimeoutException {

        SaleRequestFactory obj = new SaleRequestFactory();
        obj.setId(100);

        ProducerRecord<String, SaleRequestFactory> record = new ProducerRecord<>("tp-sale.request", obj);
        RequestReplyFuture<String, SaleRequestFactory, SaleResponseFactory> replyFuture = requestReplyKafkaTemplate.sendAndReceive(record);
        SendResult<String, SaleRequestFactory> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
        ConsumerRecord<String, SaleResponseFactory> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);


        SaleResponseFactory value = consumerRecord.value();
        System.out.println("!!!!!!!!!!!! " + value.getUnique_id());


    }
}
