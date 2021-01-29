package org.engine.rest;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.engine.plugin.transactions.factory.AuthRequestFactory;
import org.engine.plugin.transactions.factory.AuthResponseFactory;
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

    private KafkaTemplate<String, Object> requestFactoryKafkaTemplate;
    private ReplyingKafkaTemplate<String, Object, Object> requestReplyKafkaTemplate;

    @Autowired
    public CheckoutController(KafkaTemplate<String, Object> requestFactoryKafkaTemplate,
                              ReplyingKafkaTemplate<String, Object, Object> requestReplyKafkaTemplate){
        this.requestFactoryKafkaTemplate = requestFactoryKafkaTemplate;
        this.requestReplyKafkaTemplate = requestReplyKafkaTemplate;
    }

    @PostMapping("sale_test")
    public void performSaleTest() throws ExecutionException, InterruptedException, TimeoutException {

        SaleRequestFactory obj = new SaleRequestFactory();
        obj.setId(100);

        ProducerRecord<String, Object> record = new ProducerRecord<>("tp-sale.request", obj);
        RequestReplyFuture<String, Object, Object> replyFuture = requestReplyKafkaTemplate.sendAndReceive(record);
        SendResult<String, Object> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
        ConsumerRecord<String, Object> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);


        SaleResponseFactory value = (SaleResponseFactory) consumerRecord.value();
        System.out.println("!!!!!!!!!!!! " + value.getUnique_id());


    }

    @PostMapping("authorize_test")
    public void performAuthTest() throws ExecutionException, InterruptedException, TimeoutException {

        AuthRequestFactory obj = new AuthRequestFactory();
        obj.setId(140);

        ProducerRecord<String, Object> record = new ProducerRecord<>("tp-sale.request", obj);
        RequestReplyFuture<String, Object, Object> replyFuture = requestReplyKafkaTemplate.sendAndReceive(record);
        SendResult<String, Object> sendResult = replyFuture.getSendFuture().get(10, TimeUnit.SECONDS);
        ConsumerRecord<String, Object> consumerRecord = replyFuture.get(10, TimeUnit.SECONDS);


        AuthResponseFactory value = (AuthResponseFactory) consumerRecord.value();
        System.out.println("!!!!!!!!!!!! " + value.getUnique_id());


    }
}
