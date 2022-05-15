package com.ex.emailapi.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.ex.emailapi.EmailApiApplication;
import com.ex.emailapi.entities.DailyRecipeTracker;
import com.ex.emailapi.entities.Subscription;
import com.ex.emailapi.repositories.DailyRecipeTrackerRepository;
import com.ex.emailapi.repositories.SubscriptionRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class EmailServiceImpl implements EmailService{

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    DailyRecipeTrackerRepository dailyRecipeTrackerRepository;


    final Logger logger = LoggerFactory.getLogger(EmailApiApplication.class);

    @Override
    public String sendmail(String email, int recipeId) throws MessagingException{
        if(email == null || recipeId == 0){
            throw new IllegalStateException("Email id and message can't be null");
        }

        // Host url
        String host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/479101/information";
        String charset = "application/json";
        // Headers for a request
        String x_rapidapi_host = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
        String x_rapidapi_key = "46c9581dbcmsh496a852afc52dadp18d0c6jsn88d3b880b345";
        HttpResponse <JsonNode> response = null;
        try {
            response = Unirest.get(host)
                    .header("x-rapidapi-host", x_rapidapi_host)
                    .header("x-rapidapi-key", x_rapidapi_key)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        //System.out.println(response.getBody().getObject().getInt("id"));
        //System.out.println(response.getBody().getObject().getString("title"));
        String message=(response.getBody().getObject().getString("instructions"));


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
        msg.setSubject("Reimbursement email");
        msg.setContent(message, "text/html");
        msg.setSentDate(new Date());
        Transport.send(msg);

        return (response.getBody().toString());
    }

    /**
     *
     * @param emailAddressOfCurrentSubscriber This is the email address of the customer for whom a new recipe has to be emailed today
     * @param preferencesOfCurrentSubscriber This is the preference chosen by this customer for daily recipe suggestion
     * @return Sends back recipe id of a new recipe which is not send before in the past week
     */
    @Override
    public int getNewDailyRecipeForCurrentCustomer(String emailAddressOfCurrentSubscriber, String preferencesOfCurrentSubscriber) {
        List<DailyRecipeTracker> allRecipesSendToCurrentSubscriberPreviously = dailyRecipeTrackerRepository.findAllByEmail(emailAddressOfCurrentSubscriber);
        int totalNumberOfRecipesSendToCurrentSubscriberPreviously = allRecipesSendToCurrentSubscriberPreviously.size();

        int finalRecipeToBeSend = 0;
        logger.debug("Api call is being made for a random recipe");
        String host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=1";
        if(preferencesOfCurrentSubscriber.equals("NO")){
            host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=1";
        }else {
            host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?tags=" + preferencesOfCurrentSubscriber + "&number=1";
        }
        String charset = "application/json";
        String x_rapidapi_host = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
        String x_rapidapi_key = "46c9581dbcmsh496a852afc52dadp18d0c6jsn88d3b880b345";
        HttpResponse <JsonNode> response = null;
        do {

            try {
                response = Unirest.get(host)
                        .header("x-rapidapi-host", x_rapidapi_host)
                        .header("x-rapidapi-key", x_rapidapi_key)
                        .asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            String jsonString = response.getBody().getObject().getJSONArray("recipes").get(0).toString();
            JSONObject obj = new JSONObject(jsonString);
            int generatedRecipeId = obj.getInt("id");//This is the id of the random generated recipe
            System.out.println("Generated id"+ generatedRecipeId);

            //This loop make sure that the generated recipe was not send to customer within last 7 days
            for (int j = 1; j <= 7; j++) {
                if ((totalNumberOfRecipesSendToCurrentSubscriberPreviously - j) <= 0) {
                    finalRecipeToBeSend = generatedRecipeId;
                    break;
                }

                if (allRecipesSendToCurrentSubscriberPreviously.get(totalNumberOfRecipesSendToCurrentSubscriberPreviously - j).getRecipeId() == generatedRecipeId) {
                    finalRecipeToBeSend = 0;
                    break;
                } else {
                    finalRecipeToBeSend = generatedRecipeId;
                }

            }
        }while (finalRecipeToBeSend == 0);

        return finalRecipeToBeSend;
    }



    @Override
    @Scheduled(fixedRate = 150000)
    public void dailyEmailSender() {
        logger.debug("Daily email sender started");

        List<Subscription> allSubscribedUsers = subscriptionRepository.findAll();
        //Finds total number of subscribers
        int numberOfSubscribers = subscriptionRepository.findAll().size();

        //For loop to iterate through the entire list of subscribers in subscription table
        for(int i=0;i<numberOfSubscribers; i++){
            //System.out.println(allSubscribedUsers.get(i));
            logger.debug("Starting to traverse through the entire list of subscribers");

            //Selecting each individual subscriber for sending email and separately accessing the specific details
            Subscription subscriberToSendEmail = allSubscribedUsers.get(i);
            String emailAddressOfCurrentSubscriber = subscriberToSendEmail.getEmail();
            String preferencesOfCurrentSubscriber = subscriberToSendEmail.getPreferences();
            int todaysRecipeToBeSend = 0;
            if(preferencesOfCurrentSubscriber == null){
                todaysRecipeToBeSend = getNewDailyRecipeForCurrentCustomer(emailAddressOfCurrentSubscriber, "NO");
            }else {
                todaysRecipeToBeSend = getNewDailyRecipeForCurrentCustomer(emailAddressOfCurrentSubscriber, preferencesOfCurrentSubscriber);
            }
            System.out.println("Emailing recipe id "+todaysRecipeToBeSend+ "to " +emailAddressOfCurrentSubscriber);

            DailyRecipeTracker todaysRecipe = new DailyRecipeTracker();
            todaysRecipe.setEmail(emailAddressOfCurrentSubscriber);
            todaysRecipe.setRecipeId(todaysRecipeToBeSend);
            logger.debug("Trying to send an email to customer with a new recipe");
            try {
                sendmail(emailAddressOfCurrentSubscriber,todaysRecipeToBeSend);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
            logger.debug("Updated recipe tracker table with the recipe sent today");
            dailyRecipeTrackerRepository.save(todaysRecipe);
        }


    }
}
