// PendingEmail.java
package com.appreman.app.Email;

import java.io.File;

public class PendingEmail {
    private String recipient;
    private String subject;
    private String messageBody;
    private File attachment;

    public PendingEmail(String recipient, String subject, String messageBody, File attachment) {
        this.recipient = recipient;
        this.subject = subject;
        this.messageBody = messageBody;
        this.attachment = attachment;
    }

    // Getters
    public String getRecipient() { return recipient; }
    public String getSubject() { return subject; }
    public String getMessageBody() { return messageBody; }
    public File getAttachment() { return attachment; }
}
