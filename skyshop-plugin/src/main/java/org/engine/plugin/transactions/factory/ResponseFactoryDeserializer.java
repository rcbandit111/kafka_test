package org.engine.plugin.transactions.factory;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class ResponseFactoryDeserializer implements Serializable, Deserializer<AuthResponseFactory> {

    @Override
    public AuthResponseFactory deserialize(String topic, byte[] data)
    {
        AuthResponseFactory authResponseFactory = null;
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            authResponseFactory = (AuthResponseFactory) in.readObject();
            in.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Unhandled", e);
        }
        return authResponseFactory;
    }
}
