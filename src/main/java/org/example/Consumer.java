package org.example;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Consumer {
    AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
    String D_queueUrl;
    String S_queueUrl;

    List<String> Consumed = new ArrayList<>();
    Consumer(String dq, String sq){
        D_queueUrl = sqs.getQueueUrl(dq).getQueueUrl();
        S_queueUrl = sqs.getQueueUrl(sq).getQueueUrl();
    }

    void ConsumeMethod(){
        int httpStatusCode = 0;
        GetQueueAttributesResult QueueAttributes = sqs.getQueueAttributes(
                new GetQueueAttributesRequest(S_queueUrl)
                        .withAttributeNames("ApproximateNumberOfMessages"));

        int num = Integer.parseInt(QueueAttributes.getAttributes().get("ApproximateNumberOfMessages"));
        for (int i=0;i<num;i++) {


            // TODO : Pulling messages from SourceQueue
            List<Message> messages = sqs.receiveMessage(S_queueUrl).getMessages();


            // TODO : Processing the message
            int temp = Integer.parseInt(messages.get(0).getBody());
            temp = temp * temp;


            // TODO : Send the Modified Message to DestinationQueue
            try {
                SendMessageRequest send_msg_request = new SendMessageRequest()
                        .withQueueUrl(D_queueUrl)
                        .withMessageBody(
                                String.valueOf(temp)
                        )
                        .withDelaySeconds(1);

                //* Storing the HttpResponseStatusCode of @send_msg_request method for future references
                httpStatusCode = sqs.sendMessage(send_msg_request).getSdkHttpMetadata().getHttpStatusCode();

                Consumed.add(String.valueOf(temp));
                sleep(1000);
            }catch (Exception e){
                System.out.println("Exception :- " + e);
            }


            // TODO :-
                //? If the message is successfully sent to DestinationQueue, delete the message from SourceQueue
                //! Otherwise return the ERROR message along with MessageID
            if(httpStatusCode == 200){
                System.out.println("StatusCode of DeleteRequest : " + sqs.deleteMessage(
                        S_queueUrl,
                        messages.get(0).getReceiptHandle()).getSdkHttpMetadata().getHttpStatusCode());
            }
            else {
                System.out.printf(
                        "Error !!  StatusCode : %d  MessageId : %s",
                        httpStatusCode,
                        messages.get(0).getMessageId());
            }
        }
        System.out.println("Order of Consumption :- " + Consumed);
    }
}