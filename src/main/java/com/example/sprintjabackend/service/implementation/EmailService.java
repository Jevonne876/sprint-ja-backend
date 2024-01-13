package com.example.sprintjabackend.service.implementation;

import com.example.sprintjabackend.model.User;
import com.example.sprintjabackend.model.UserPrincipal;
import com.example.sprintjabackend.repository.UserRepository;
import com.example.sprintjabackend.utility.JwtTokenProvider;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.example.sprintjabackend.constant.EmailConstant.*;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

@Service
public class EmailService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public EmailService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void newUserEmail(String firstName, String email) throws MessagingException {
        Message message = createEmail(firstName, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    public void sendingQuery(String fullName, String email, String number, String text) throws MessagingException {
        Message message = queryEmail(fullName, email, number, text);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }



    public void sendNewPackageEmail(String firstName, String lastName, Long trn, String trackingNumber, String courier, String description, double weight, double cost) throws MessagingException {
        Message message = newPackageCreated(firstName, lastName, trn, trackingNumber, courier, description, weight, cost);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    public void sendNewAccountPassword(String firstName, String lastname, String password, String email) throws MessagingException {
        Message message = createUserAndEmailPassword(firstName, lastname, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }



    public void sendMessage(String recipient, String subject, String text) throws MessagingException {
        Message message = sendEmail(recipient, subject, text);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    public void sendBroadCastMessage(List<String> emails, String subject, String text) throws MessagingException {
        for (String email : emails) {
            Message message = sendEmail(email, subject, text);
            SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
            smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
        }
    }

    public void sendPackageStatusUpdateEmail(String firstName, String lastName, String trackingNumber, String status, String email) throws MessagingException {
        Message message = createEmail(firstName, lastName, trackingNumber, status, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();

    }

    private Message createEmail(String firstName, String password, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Hello " + firstName + ", \n \n Your new account password is: " + password + "\n \n SprintJA Limited");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }


    private Message createEmail(String firstName, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("New USER");
        message.setText("Hello " + firstName + ", Welcome to our family " + "\n \n Regards, SprintJA Limited");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Message createEmail(String firstName, String lastName, String trackingNumber, String status, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("PACKAGE UPDATE");
        message.setText("Hello: " + firstName + " " + lastName + ", your pack with tracking number:  " + trackingNumber + " status was updated to: " + status + ".\n"
                + "Please upload invoice if you have not do so as yet." + "\n \n Regards, SprintJA Limited");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }


    private Message queryEmail(String fullName, String email, String number, String text) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(FROM_EMAIL, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("NEW QUERY");
        message.setText("FullName:" + fullName + "  \n\n" + "Email: " + email + " \n\n" + "PhoneNumber: " + number + "\n\n " + " Query: " + text);
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    private Message newPackageCreated(String firstName, String lastName, Long trn, String trackingNumber, String courier, String description, double weight, double cost) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(FROM_EMAIL, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("NEW PRE-ALERT");
        message.setText("A new Pre-Alert was created by: " + firstName + " " + lastName + ", TRN :" + trn + "\n\n" +
                "Tracking Number :" + trackingNumber + "\n\n" + "Courier :" + courier + "\n\n" +
                "Description :" + description + "\n\n" + "Weight :" + weight + "\n\n" + "Cost :" + cost);
        message.setSentDate(new Date());
        message.saveChanges();
        return message;

    }

    private Message createUserAndEmailPassword(String firstName, String lastname, String password, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Hello " + firstName + " " + lastname + ", a user account was created by our system administrator for you." + ", \n \n Your new account password is: " + password + "\n \n SprintJa");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }


    private Message sendEmail(String recipient, String subject, String text) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(recipient, false));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(subject);
        message.setText(text + "\n \n SprintJa");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;

    }


    private Message sendBroadCastEmail(String email, String subject, String text) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(CC, InternetAddress.parse(CC_EMAIL, false));
        message.setRecipients(TO, InternetAddress.parse(email, false));
        message.setSubject(subject);
        message.setText(text + "\n \n SprintJa");
        message.setSentDate(new Date());
        message.saveChanges();

        return message;
    }


    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);
        return Session.getInstance(properties, null);
    }
}
