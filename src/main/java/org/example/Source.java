package org.example;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Source {
    AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    String S_queueUrl;

    public Source(String sq){
        S_queueUrl = sqs.getQueueUrl(sq).getQueueUrl();
    }
    void SourceMethod() {
        List<String> ls = new ArrayList<>();
        for(int i=1;i<11;i++){
            ls.add(String.valueOf(i));
        }
//        System.out.println(ls);
        try {
            for (String l : ls) {
                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(S_queueUrl)
                        .withMessageBody(
                                l
                        )
                        .withDelaySeconds(1);
                sqs.sendMessage(send_msg_request);
                sleep(2000);
            }
        } catch (Exception e){
            System.out.println("Exception :- "+e);
        }
        System.out.println("Done !");
    }
}
