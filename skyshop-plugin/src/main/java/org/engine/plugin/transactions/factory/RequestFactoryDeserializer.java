package org.engine.plugin.transactions.factory;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class RequestFactoryDeserializer implements Serializable, Deserializer<Object> {

    @Override
    public Object deserialize(String topic, byte[] data)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try
        {
            ObjectInputStream in = new ObjectInputStream(bis);
            in.close();
            return in.readObject();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Unhandled", e);
        }
    }
}
