package org.engine.plugin.transactions.factory;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class SaleResponseFactoryDeserializer implements Serializable, Deserializer<SaleResponseFactory> {

    @Override
    public SaleResponseFactory deserialize(String topic, byte[] data)
    {
        SaleResponseFactory saleResponseFactory = null;
        try
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            saleResponseFactory = (SaleResponseFactory) in.readObject();
            in.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException("Unhandled", e);
        }
        return saleResponseFactory;
    }
}
