package org.engine.plugin.transactions.factory;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class SaleRequestFactoryDeserializer implements Serializable, Deserializer<SaleRequestFactory> {

    @Override
    public SaleRequestFactory deserialize(String topic, byte[] data)
    {
        SaleRequestFactory saleRequestFactory = null;
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            saleRequestFactory = (SaleRequestFactory) in.readObject();
            in.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Unhandled", e);
        }
        return saleRequestFactory;
    }
}
