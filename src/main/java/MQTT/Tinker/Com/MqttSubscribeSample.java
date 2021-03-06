package MQTT.Tinker.Com;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import java.io.IOException;
import java.security.Timestamp;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
/**
 * Created by mark on 20/03/16.
 */
public class MqttSubscribeSample implements MqttCallback {
    MqttClient sampleClient;
    MqttConnectOptions connOpts;
    String content      = "Message from MqttPublishSample";
    int qos             = 2;
    String broker       = "tcp://iot.eclipse.org:1883";
    String clientId     = "JavaSample";
    MemoryPersistence persistence = new MemoryPersistence();
    String topic        = "MQTT Examples";

    public static void main(String[] args) {
        MqttSubscribeSample MSS = new MqttSubscribeSample();
        MSS.RunClient();
    }


public void RunClient()
    {
        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            sampleClient.setCallback(this);
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            //System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            //sampleClient.publish(topic, message);
            sampleClient.subscribe(topic,qos);
            System.out.println("Message Subscribed");
            try {
                // wait to ensure subscribed messages are delivered
                Thread.sleep(30000);
                sampleClient.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }


    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {

        System.out.println("\nReceived a Message!" +
                "\n\tTopic:   " + topic +
                "\n\tMessage: " + new String(message.getPayload()) +
                "\n\tQoS:     " + message.getQos() + "\n");
         // unblock main thread
    }

    public void connectionLost(Throwable cause) {
        System.out.println("Connection to Solace broker lost!" + cause.getMessage());
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
    }

}

