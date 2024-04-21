package com.hackathon.transactionservice.config;

import com.hackathon.transactionservice.codec.OffsetDateTimeCodec;
import com.hackathon.transactionservice.converter.DateToOffsetDateTimeConverter;
import com.hackathon.transactionservice.converter.OffsetDateTimeToDateConverter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    private final List<Converter<?,?>> converters = new ArrayList<>();

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.database}")
    private String database;


    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Bean
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString(uri);
        CodecRegistry codecRegistry = fromRegistries(CodecRegistries.fromCodecs(
                new OffsetDateTimeCodec()), MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder().
                applyConnectionString(connectionString).codecRegistry(codecRegistry).build();

        return MongoClients.create(mongoClientSettings);
    }

    @Override
    public MongoCustomConversions customConversions() {
        converters.add(new OffsetDateTimeToDateConverter());
        converters.add(new DateToOffsetDateTimeConverter());
        return new MongoCustomConversions(converters);
    }
}