package psychologist.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import psychologist.project.config.BookingConfig;
import psychologist.project.dto.payment.CreatePaymentDto;
import psychologist.project.dto.payment.PaymentDto;
import psychologist.project.dto.payment.PaymentPsychologistDto;
import psychologist.project.service.payment.PaymentService;

@Tag(name = "Payment controller",
        description = "Payments management endpoint")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    private final BookingConfig bookingConfig;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    @Operation(summary = "Get all payments",
            description = "Get all payments in system [ADMIN ONLY]")
    public List<PaymentDto> getAllPayments(@ParameterObject Pageable pageable) {
        return paymentService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN') or "
            + " #userId == @securityUtil.loggedInUserId")
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get payments of some user",
            description = "Get payments by user by his id if authorized as that user,"
                    + " or as ADMIN role user")
    public List<PaymentDto> getPaymentsByUserId(@PathVariable Long userId,
                                                @ParameterObject Pageable pageable) {
        return paymentService.getAllByUserId(pageable, userId);
    }

    @GetMapping("/success")
    @Operation(summary = "Confirm payment",
            description = "Confirm payment with given session")
    public RedirectView confirmPayment(@RequestParam(required = false) String sessionId) {
        PaymentPsychologistDto success = paymentService.success(sessionId);
        return new RedirectView(bookingConfig.getRedirect() + success.getPsychologistId());
    }

    @GetMapping("/cancel")
    @Operation(summary = "Cancel payment",
            description = "Cancel payment with given session")
    public RedirectView cancelPayment(@RequestParam(required = false) String sessionId) {
        PaymentPsychologistDto cancel = paymentService.cancel(sessionId);
        return new RedirectView(bookingConfig.getRedirect() + cancel.getPsychologistId());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    @PatchMapping("/update/{paymentId}")
    @Operation(summary = "Renew payment",
            description = "Renew expired payment")
    public PaymentDto renewPayment(@PathVariable Long paymentId) {
        return paymentService.renew(paymentId);
    }

    @PostMapping
    @Operation(summary = "Create payment",
            description = "Create payment entity with session based on booking data")
    public PaymentDto createPayment(@RequestBody CreatePaymentDto createPaymentDto) {
        return paymentService.save(createPaymentDto);
    }

    @GetMapping("create")
    @Operation(summary = "Create payment by url",
            description = "Create payment entity as unauthorized user")
    public PaymentDto createPaymentFromUrl(@RequestParam Long bookingId) {
        return paymentService.save(new CreatePaymentDto().setBookingId(bookingId));
    }
}
