package com.mongodb.flywheel;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Scanner;

@SpringBootApplication
public class Prereqtest implements CommandLineRunner {

    @Autowired
    private MongoClient mongoClient;

    public static void main(String[] args) {
        SpringApplication.run(Prereqtest.class, args);
    }

    @Override
    public void run(String... args) {

            //Ignore this code - this is not how you normally use MongoDB in spring, this is much
        // more low level to keep it very short.
        try {
            MongoDatabase database = mongoClient.getDatabase("admin");
            Document pingResult = database.runCommand(new Document("hello", 1));
            String key = pingResult.getString("setName");

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your email address: ");
            String email = scanner.nextLine();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            key = key+"_"+email;
            byte[] hash = digest.digest(key.getBytes(StandardCharsets.UTF_8));
            // Convert first 2 bytes to 4 hex characters
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                sb.append(String.format("%02x", hash[i]));
            }
            System.out.println("Contact MongoDB with this code to confirm your place on the course: "+ sb);
        } catch (Exception e) {
            System.err.println("Failure to make connection to MongoDB Atlas or configured DB");
            System.err.println(e.getMessage());
        }
    }
}