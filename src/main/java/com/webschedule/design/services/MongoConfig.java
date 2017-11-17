/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webschedule.design.services;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 *
 * @author ivaylo
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {
  
    @Override
    protected String getDatabaseName() {
        return "test";
    }
  
    @Override
    public Mongo mongo() throws Exception {
        MongoClientURI mURI = new MongoClientURI("mongodb://websch_client:q2SosiDcF4148vry@ds042677.mlab.com:42677/webschedule");
        return new MongoClient(mURI);
    }
}
