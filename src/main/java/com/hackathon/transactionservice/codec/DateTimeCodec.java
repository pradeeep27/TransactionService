package com.hackathon.transactionservice.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Instant;
import java.util.Date;

public class DateTimeCodec implements Codec<Date> {
    @Override
    public Date decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return Date.from(Instant.ofEpochMilli(bsonReader.readDateTime()));
    }

    @Override
    public void encode(BsonWriter bsonWriter, Date date, EncoderContext encoderContext) {
        bsonWriter.writeDateTime(date.toInstant().toEpochMilli());
    }

    @Override
    public Class<Date> getEncoderClass() {
        return Date.class;
    }
}
