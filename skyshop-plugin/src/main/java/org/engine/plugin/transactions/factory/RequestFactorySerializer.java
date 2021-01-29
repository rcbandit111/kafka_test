package org.engine.plugin.transactions.factory;

import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class RequestFactorySerializer implements Serializable, Serializer<Object> {

    @Override
    public byte[] serialize(String topic, Object data)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(data);
            out.close();
            return out.toByteArray();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unhandled", e);
        }
    }
}
