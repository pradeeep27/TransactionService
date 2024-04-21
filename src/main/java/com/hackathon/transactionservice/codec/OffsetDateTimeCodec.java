package com.hackathon.transactionservice.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class OffsetDateTimeCodec implements Codec<OffsetDateTime> {


    @Override
    public OffsetDateTime decode(BsonReader bsonReader, DecoderContext decoderContext) {
        long epochMilli = bsonReader.readDateTime();
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC);
    }

    @Override
    public void encode(BsonWriter bsonWriter, OffsetDateTime offsetDateTime, EncoderContext encoderContext) {
        bsonWriter.writeDateTime(offsetDateTime.toInstant().toEpochMilli());
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }
}
