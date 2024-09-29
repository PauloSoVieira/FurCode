package org.mindera.fur.code.model;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static javax.mail.Message.RecipientType.TO;

/**
 * Utility class for sending emails using Gmail.
 */
@Component
public class Gmailer {


    private static final String TEST_EMAIL = "test@mail";
    private Gmail service;

    /**
     * Constructs a new Gmailer instance and initializes the Gmail service.
     *
     * @throws Exception if there's an error initializing the Gmail service
     */
    public Gmailer() throws Exception {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT, jsonFactory))
                .setApplicationName("Test Application")
                .build();
    }

    /**
     * Retrieves OAuth2 credentials for Gmail API.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport
     * @param jsonFactory    JsonFactory for handling JSON
     * @return Credential object for OAuth2 authentication
     * @throws IOException if there's an error reading client secrets or storing credentials
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, GsonFactory jsonFactory)
            throws IOException {

        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(Gmailer.class.getResourceAsStream("/client_secret.json")));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GmailScopes.GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

    }


    /**
     * Main method for testing the Gmailer functionality.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            Gmailer gmailer = new Gmailer();
            gmailer.sendMail("test@mail", "Não é scam ", "Test email");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends an email using the Gmail API.
     *
     * @param to      The recipient's email address
     * @param subject The subject of the email
     * @param message The body of the email
     * @throws Exception if there's an error sending the email
     */
    public void sendMail(String to, String subject, String message) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(TEST_EMAIL));
        email.addRecipient(TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(message);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            msg = service.users().messages().send("me", msg).execute();
            System.out.println("Message sent. ID: " + msg.getId());
        } catch (GoogleJsonResponseException e) {
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
    }
}

//package org.mindera.fur.code.model;
//
//import com.google.api.client.auth.oauth2.Credential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
//import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
//import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.googleapis.json.GoogleJsonError;
//import com.google.api.client.googleapis.json.GoogleJsonResponseException;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.client.util.store.FileDataStoreFactory;
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.GmailScopes;
//import com.google.api.services.gmail.model.Message;
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.stereotype.Component;
//
//import javax.mail.Session;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.file.Paths;
//import java.util.Properties;
//import java.util.Set;
//
//import static javax.mail.Message.RecipientType.TO;
//
///**
// * Utility class for sending emails using Gmail.
// */
//
//public class Gmailer {
//
//
//    private static final String TEST_EMAIL = "paulo.vieira@minderacodeacademy.com";
//    private Gmail service;
//
//    /**
//     * Constructs a new Gmailer instance and initializes the Gmail service.
//     *
//     * @throws Exception if there's an error initializing the Gmail service
//     */
//    public Gmailer() throws Exception {
//        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
//        service = new Gmail.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT, jsonFactory))
//                .setApplicationName("Test Application")
//                .build();
//    }
//
//    /**
//     * Retrieves OAuth2 credentials for Gmail API.
//     *
//     * @param HTTP_TRANSPORT The network HTTP Transport
//     * @param jsonFactory    JsonFactory for handling JSON
//     * @return Credential object for OAuth2 authentication
//     * @throws IOException if there's an error reading client secrets or storing credentials
//     */
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT, GsonFactory jsonFactory)
//            throws IOException {
//
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(jsonFactory, new InputStreamReader(Gmailer.class.getResourceAsStream("/client_secret.json")));
//
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, jsonFactory, clientSecrets, Set.of(GmailScopes.GMAIL_SEND))
//                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//
//    }
//
//
//    /**
//     * Main method for testing the Gmailer functionality.
//     *
//     * @param args Command line arguments (not used)
//     */
//    public static void main(String[] args) {
//        try {
//            Gmailer gmailer = new Gmailer();
//            gmailer.sendMail("paulo.vieira@minderacodeacademy.com", "Não é scam ", "Test email");
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Sends an email using the Gmail API.
//     *
//     * @param to      The recipient's email address
//     * @param subject The subject of the email
//     * @param message The body of the email
//     * @throws Exception if there's an error sending the email
//     */
//    public void sendMail(String to, String subject, String message) throws Exception {
//        Properties props = new Properties();
//        Session session = Session.getDefaultInstance(props, null);
//        MimeMessage email = new MimeMessage(session);
//        email.setFrom(new InternetAddress(TEST_EMAIL));
//        email.addRecipient(TO, new InternetAddress(to));
//        email.setSubject(subject);
//        email.setText(message);
//
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        email.writeTo(buffer);
//        byte[] rawMessageBytes = buffer.toByteArray();
//        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
//        Message msg = new Message();
//        msg.setRaw(encodedEmail);
//
//        try {
//            msg = service.users().messages().send("me", msg).execute();
//            System.out.println("Message sent. ID: " + msg.getId());
//        } catch (GoogleJsonResponseException e) {
//            GoogleJsonError error = e.getDetails();
//            if (error.getCode() == 403) {
//                System.err.println("Unable to send message: " + e.getDetails());
//            } else {
//                throw e;
//            }
//        }
//    }
//
//}
