package com.appreman.app.Email;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;

public class MailSender {

    private final Queue<PendingEmail> pendingEmails = new LinkedList<>();

    public MailSender() {
    }

    public void sendMail(String recipient, String subject, String messageBody, Context context) {
        if (recipient == null || recipient.isEmpty()) {
            Log.e("MailSender", "El destinatario es nulo o vacío");
            return;
        }
        if (isConnectedToInternet(context)) {
            try {
                sendMailInternal(recipient, subject, messageBody);
                Log.d("MailSender", "Correo enviado exitosamente a: " + recipient);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MailSender", "Error al enviar el correo: " + e.getMessage());
                pendingEmails.add(new PendingEmail(recipient, subject, messageBody, null));
            }
        } else {
            pendingEmails.add(new PendingEmail(recipient, subject, messageBody, null));
        }
    }

    public void sendMailWithAttachment(String recipient, String subject, String messageBody, File file, Context context) {
        if (recipient == null || recipient.isEmpty()) {
            Log.e("MailSender", "El destinatario es nulo o vacío");
            return;
        }
        if (isConnectedToInternet(context)) {
            try {
                sendMailWithAttachmentInternal(recipient, subject, messageBody, file);
                Log.d("MailSender", "Correo enviado exitosamente a: " + recipient);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MailSender", "Error al enviar el correo: " + e.getMessage());
                pendingEmails.add(new PendingEmail(recipient, subject, messageBody, file));
            }
        } else {
            pendingEmails.add(new PendingEmail(recipient, subject, messageBody, file));
        }
    }

    private void sendMailInternal(String recipient, String subject, String messageBody) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Credentials.USERNAME, Credentials.PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Credentials.USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(messageBody);

        Transport.send(message);
    }

    private void sendMailWithAttachmentInternal(String recipient, String subject, String messageBody, File file) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Credentials.USERNAME, Credentials.PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(Credentials.USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(messageBody);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.setDataHandler(new DataHandler(new FileDataSource(file)));
        attachmentPart.setFileName(file.getName());

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    public boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public void addPendingEmail(PendingEmail email) {
        pendingEmails.add(email);
    }

    public void retryPendingEmails(Context context) {
        if (isConnectedToInternet(context)) {
            Log.d("MailSender", "Conexión establecida con el servidor");
        } else {
            Log.d("MailSender", "No hay conexión a Internet");
        }
        while (!pendingEmails.isEmpty() && isConnectedToInternet(context)) {
            PendingEmail email = pendingEmails.poll();
            Log.d("MailSender", "Reintentando enviar correo a: " + email.getRecipient());
            try {
                if (email.getAttachment() == null) {
                    sendMailInternal(email.getRecipient(), email.getSubject(), email.getMessageBody());
                } else {
                    sendMailWithAttachmentInternal(email.getRecipient(), email.getSubject(), email.getMessageBody(), email.getAttachment());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MailSender", "Error al enviar el correo pendiente: " + e.getMessage());
                pendingEmails.add(email);
                break;
            }
        }
    }

    public void checkInbox(Context context) {
        if (isConnectedToInternet(context)) {
            try {
                Properties props = new Properties();
                props.put("mail.store.protocol", "imaps");
                Session session = Session.getInstance(props, null);
                Store store = session.getStore();
                store.connect("imap.gmail.com", Credentials.USERNAME, Credentials.PASSWORD);

                Folder inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                Message[] messages = inbox.getMessages();
                for (Message message : messages) {
                    Log.d("MailSender", "Correo recibido de: " + message.getFrom()[0]);
                    Log.d("MailSender", "Asunto: " + message.getSubject());
                }

                inbox.close(false);
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
                Log.e("MailSender", "Error al recibir correos: " + e.getMessage());
            }
        } else {
            Log.d("MailSender", "No hay conexión a Internet");
        }
    }
}
