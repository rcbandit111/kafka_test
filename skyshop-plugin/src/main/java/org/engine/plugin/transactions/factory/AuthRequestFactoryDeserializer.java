package org.engine.plugin.transactions.factory;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class AuthRequestFactoryDeserializer implements Serializable, Deserializer<AuthRequestFactory> {

    @Override
    public AuthRequestFactory deserialize(String topic, byte[] data)
    {
        AuthRequestFactory authRequestFactory = null;
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            authRequestFactory = (AuthRequestFactory) in.readObject();
            in.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Unhandled", e);
        }
        return authRequestFactory;
    }
}
