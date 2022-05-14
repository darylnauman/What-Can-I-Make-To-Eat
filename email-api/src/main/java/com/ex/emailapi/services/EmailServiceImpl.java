package com.ex.emailapi.services;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class EmailServiceImpl implements EmailService{
    @Override
    public String sendmail(String email, int recipeId) throws AddressException, MessagingException, IOException {
        if(email == null || recipeId == 0){
            throw new IllegalStateException("Email id and message can't be null");
        }

        // Host url
        String host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/479101/information";
        String charset = "application/json";
        // Headers for a request

       // String x_rapidapi_host = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
       // String x_rapidapi_key = "46c9581dbcmsh496a852afc52dadp18d0c6jsn88d3b880b345";

        String x_rapidapi_host = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
        String x_rapidapi_key = "8225095afcmsh9855bc73d24b31cp145800jsn918dfa7503e8";

        HttpResponse <JsonNode> response = null;
        try {
            response = Unirest.get(host)
                    .header("x-rapidapi-host", x_rapidapi_host)
                    .header("x-rapidapi-key", x_rapidapi_key)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        System.out.println(response.getBody().getObject().getInt("id"));
        System.out.println(response.getBody().getObject().getString("title"));

        //String message=(response.getBody().getObject().getString("instructions"));
        String message=("<html><body><h2>"+"Instructions </h2>"+response.getBody().getObject().getString("instructions")+"<br><h2>servings </h2><h3>"+response.getBody().getObject().getInt("servings")+"</h3>" +
                "<h3>Total Time </h3><h4>"+response.getBody().getObject().getInt("readyInMinutes")+" "+" minutes</h4>"
        +"</body></html>");


        String subject = response.getBody().getObject().getString("title");


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");


        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("AneeshRevatureProject1@gmail.com", "RevatureBank2022");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("AneeshRevatureProject1@gmail.com", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("Your Favorite Recipe - "+subject);
        msg.setContent(message, "text/html");
        msg.setSentDate(new Date());
        Transport.send(msg);

        return (response.getBody().toString());
    }
}
