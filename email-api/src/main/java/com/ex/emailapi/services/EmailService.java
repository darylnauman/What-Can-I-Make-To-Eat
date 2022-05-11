package com.ex.emailapi.services;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;

public interface EmailService {
    void sendmail(String emailId, String message) throws AddressException, MessagingException, IOException;

}
