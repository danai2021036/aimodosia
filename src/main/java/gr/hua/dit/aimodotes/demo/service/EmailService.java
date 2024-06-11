package gr.hua.dit.aimodotes.demo.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    SendGrid sendGrid;

//    public Response sendemail(EmailRequest emailrequest)
//    {
//
//        Mail mail = new Mail(new Email("it2021077@hua.gr"), emailrequest.getSubject(), new Email(emailrequest.getTo()),new Content("text/plain", emailrequest.getBody()));
//        mail.setReplyTo(new Email("it2021036@hua.gr"));
//        Request request = new Request();
//
//        Response response = null;
//
//        try {
//
//            request.setMethod(Method.POST);
//
//            request.setEndpoint("mail/send");
//
//            request.setBody(mail.build());
//
//            response=this.sendGrid.api(request);
//
//        } catch (IOException ex) {
//
//            System.out.println(ex.getMessage());
//
//        }
//
//        return response;
//    }

    //    @Value("${app.sendgrid.templateId}")
//    private String templateId;
//
//    public String sendEmail(String email)  {
//
//        try {
//            Mail mail = prepareMail(email);
//
//            Request request = new Request();
//
//            request.setMethod(Method.POST);
//            request.setEndpoint("mail/send");
//
//            request.setBody(mail.build());
//
//
//            Response response = sendGrid.api(request);
//
//            if(response!=null) {
//
//                System.out.println("response code from sendgrid"+response.getHeaders());
//
//            }
//
//        } catch (IOException e) {
//
//
//            e.printStackTrace();
//            return "error in sent mail!";
//        }
//
//        return "mail has been sent check your inbox!";
//
//    }
//
//    public Mail prepareMail(String email) {
//
//        Mail mail = new Mail();
//
//        Email fromEmail = new Email();
//
//        fromEmail.setEmail("naf.pap2003@gmail.com");
//
//        mail.setFrom(fromEmail);
//        Email to = new Email();
//        to.setEmail(email);
//
//
//        Personalization personalization = new Personalization();
//
//        personalization.addTo(to);
//        mail.addPersonalization(personalization);
//
//        mail.setTemplateId(templateId);
//
//        return mail;
//    }
    public void sendEmail(String to, String subject, String body) throws IOException {
        Email from = new Email("dyvri15dyvri@yahoo.gr");
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
}

