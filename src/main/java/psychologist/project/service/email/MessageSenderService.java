package psychologist.project.service.email;

import jakarta.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import psychologist.project.dto.auth.UserRegisterResponseDto;
import psychologist.project.dto.booking.BookingWithPsychologistInfoDto;
import psychologist.project.dto.payment.PaymentDto;
import psychologist.project.dto.psychologist.PsychologistWithDetailsDto;
import psychologist.project.model.Booking;
import psychologist.project.model.User;
import psychologist.project.repository.user.UserRepository;

@Service
@RequiredArgsConstructor
public class MessageSenderService {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private final EmailService emailService;
    private final UserRepository userRepository;

    @Value("${backend.url}")
    private String backendUrl;
    @Value("${main.redirect}")
    private String mainUrl;

    @Async
    public void onPaymentCreation(PaymentDto paymentDto,
                                  BookingWithPsychologistInfoDto bookingData) {
        User user = receiveUser(bookingData.getUserId());
        PsychologistWithDetailsDto psychologist = bookingData.getPsychologistDto();
        String cancelUrl = backendUrl + "api/payments/cancel?sessionId="
                + paymentDto.getSessionId();
        String html = "<p>&nbsp;</p>"
                + "<p>Hi " + user.getFirstName() + "," + "</p>"
                + "<p>&nbsp;</p>"
                + "<p>Your payment has been created for booking</p>"
                + "<p>&nbsp;</p>"
                + "<ul>"
                + "  <li>Your Psychologist: " + psychologist.getFirstName() + " "
                + psychologist.getFatherName() + " "
                + psychologist.getLastName() + "</li>"
                + "  <li>Date & Time:" + bookingData.getStartTime()
                                        .format(formatter) + "</li>"
                + "  <li>Price: $" + psychologist.getSessionPrice() + "</li>"
                + "</ul>"
                + "<p>&nbsp;</p>"
                + "<p> You can complete your payment on link: </p>"
                + "<a href=\"" + paymentDto.getSessionUrl()
                + "\">Payment</a>"
                + "<p> You can also cancel your payment on link: </p>"
                + "<a href=\"" + cancelUrl
                + "\">Cancel</a>";

        emailService.sendHtmlEmail(user.getEmail(),
                "Complete payment for MindBloom booking", html);
    }

    @Async
    public void onCancelPayment(BookingWithPsychologistInfoDto bookingData) {
        User user = receiveUser(bookingData.getUserId());
        PsychologistWithDetailsDto psychologist = bookingData.getPsychologistDto();
        String html = "<p>&nbsp;</p>"
                + "<p>Hi " + user.getFirstName() + "," + "</p>"
                + "<p>&nbsp;</p>"
                + "<p>Your meeting session on MindBloom has been canceled</p>"
                + "<ul>"
                + "  <li>Your Psychologist: " + psychologist.getFirstName() + " "
                + psychologist.getFatherName() + " "
                + psychologist.getLastName() + "</li>"
                + "  <li>Date & Time:" + bookingData.getStartTime()
                                        .format(formatter) + "</li>"
                + "</ul>"
                + "<p>&nbsp;</p>"
                + "<p>Take care, "
                + "<p>The MindBloom Team";

        emailService.sendHtmlEmail(user.getEmail(),
                "Your MindBloom payment is canceled", html);
    }

    @Async
    public void onConfirmPayment(BookingWithPsychologistInfoDto bookingData) {
        User user = receiveUser(bookingData.getUserId());
        PsychologistWithDetailsDto psychologist = bookingData.getPsychologistDto();
        String html = "<p>&nbsp;</p>"
                + "<p>Hi " + user.getFirstName() + "," + "</p>"
                + "<p>Thank you for booking a session on MindBloom. Here are the details: </p>"
                + "<p>&nbsp;</p>"
                + "<ul>"
                    + "  <li>Your Psychologist: " + psychologist.getFirstName() + " "
                    + psychologist.getFatherName() + " "
                    + psychologist.getLastName() + "</li>"
                    + "  <li>Date & Time:" + bookingData.getStartTime()
                                             .format(formatter) + "</li>"
                    + "  <li>Session Link:" + bookingData.getMeetingUrl() + "</li>"
                + "</ul>"
                + "<p>&nbsp;</p>"
                + "<p>Take care, "
                + "<p>The MindBloom Team";

        emailService.sendHtmlEmail(user.getEmail(),
                "Your MindBloom payment is confirmed", html);
    }

    @Async
    public void onCreateBooking(Booking bookingData) {
        User user = receiveUser(bookingData.getUser().getId());
        String paymentUrl = backendUrl + "api/payments/create?bookingId=" + bookingData.getId();
        String html = "<p>Hi " + user.getFirstName() + "," + "</p>"
                + "<p>&nbsp;</p>"
                + "<p>Your meeting session on MindBloom has been created</p>"
                + "<ul>"
                + "  <li>Date & Time:" + bookingData.getStartTime()
                                          .format(formatter) + "</li>"
                + "</ul>"
                + "<p>&nbsp;</p>"
                + "<p>You can confirm this meeting with payment on the website "
                + "or in this email by link: "
                + "<a href=\"" + paymentUrl + "\">Create Payment</a>";

        emailService.sendHtmlEmail(user.getEmail(), "New session in MindBloom", html);
    }

    @Async
    public void onRegistered(UserRegisterResponseDto userData) {
        User user = receiveUser(userData.getId());
        String html = "<html>"
                + "<body>"
                + "<p>Hi " + user.getFirstName() + "," + "</p>"
                + "<p>&nbsp;</p>"
                + "<p>We’re so glad you’ve joined MindBloom — a safe and supportive space<br>"
                + "for your mental well-being.</p>"
                + "<p>&nbsp;</p>"
                + "<p>Your personal account is now ready. Click below to log in and explore our<br>"
                + "licensed therapists.</p>"
                + "<p>&nbsp;</p>"
                + "<p><a href=\"" + mainUrl + "\">Proceed to website</a></p>"
                + "<p>&nbsp;</p>"
                + "<p>Whether you're looking for support, clarity, or simply someone<br>"
                + "to talk to — we're here for you.</p>"
                + "<p>&nbsp;</p>"
                + "<p>Warm wishes,<br>"
                + "The MindBloom Team</p>"
                + "</body>"
                + "</html>";

        emailService.sendHtmlEmail(user.getEmail(), "Welcome to MindBloom", html);
    }

    private User receiveUser(Long userId) {
        return userRepository.findByIdIncludingDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));
    }
}
