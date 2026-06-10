    package com.example.expenseo.service;

    import com.example.expenseo.dto.*;
    import com.example.expenseo.mapper.UserMapStructMapper;
    import com.example.expenseo.models.UserModel;
    import com.example.expenseo.repository.UserRepository;
    import com.example.expenseo.security.CustomUserDetails;
    import com.example.expenseo.security.JwtUtils;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.security.SecureRandom;
    import java.time.LocalDateTime;

    @Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapStructMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;



        @Transactional
        public AuthResponse signUp(UserRequest user) {

            // 1. Check if email is already taken
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("Email is already in use!");
            }

            // 2. Generate a secure 6-digit OTP
            SecureRandom random = new SecureRandom();
            int otpNumber = 100000 + random.nextInt(900000); // Guarantees a 6-digit number
            String generatedOtp = String.valueOf(otpNumber);

            // 3. Map the DTO to the Entity
            UserModel newUser = userMapper.toEntity(user);

            // 4. Hash the password securely
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            newUser.setPassword(hashedPassword);

            // 5. Set up the Verification and OTP states
            newUser.setOtp(generatedOtp);
            newUser.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            newUser.setVerified(false);

            // 6. Save the user to the database
            UserModel savedUser = userRepository.save(newUser);

            // 7. Send the email containing the 6-digit OTP (not a link token!)
            emailService.sendOtpEmail(savedUser.getEmail(), generatedOtp);

            // 8. Return the mapped response
            return userMapper.toResponse(savedUser);

    }

        public void verifyOtp(OtpVerificationRequest request){
            UserModel user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(()-> new RuntimeException("User is not found"));

            if (user.isVerified()){
                throw new RuntimeException("User is already verified..!");
            }

            if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())){
                throw new RuntimeException("Invalid OTP. Please try again.");
            }

            if (user.getOtpExpiry().isBefore(LocalDateTime.now())){
                throw new RuntimeException("OTP has expired. Please request a new one.");
            }

            user.setVerified(true);

            user.setOtp(null);
            user.setOtpExpiry(null);

            userRepository.save(user);

        }

        public AuthResponse login(LoginRequest request) {
            // 1. Authenticate the user AND capture the result
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // 2. Extract the CustomUserDetails from the authentication object
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 3. Extract the raw UserModel from your wrapper (assuming you added the getUser() method we discussed earlier)
            assert userDetails != null;
            UserModel user = userDetails.getUser();

            // 4. Generate the token
            String jwt = jwtUtils.generateToken(userDetails);

            // 5. Return the response
            return AuthResponse.builder()
                    .token(jwt)
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .build();
        }

        public void resendOtp(ResendOtpRequest request){
            UserModel user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(()-> new RuntimeException("User not found with this email."));

            if (user.isVerified()){
                throw new RuntimeException("This account is already verified. Please log in.");
            }

            SecureRandom random = new SecureRandom();
            int otpNumber = 100000 + random.nextInt(900000);
            String newOtp = String.valueOf(otpNumber);

            user.setOtp(newOtp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

            userRepository.save(user);

            emailService.sendOtpEmail(user.getEmail(), newOtp);
        }

        public void updatePassword(UpdatePasswordRequest request){
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                    .getContext().getAuthentication().getPrincipal();

            UserModel user = userDetails.getUser();

            if (!user.isVerified()){
                throw new RuntimeException("User is not verified..!");
            }

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Old password is incorrect!");
            }

            String hashedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(hashedPassword);

            userRepository.save(user);

        }

        @Transactional
        public void forgetPassword(ForgotPasswordRequest request){
            UserModel user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(()->new RuntimeException("User not found with this email."));

            SecureRandom random = new SecureRandom();
            int otpNumber = 1000 + random.nextInt(9000);
            String resetOtp = String.valueOf(otpNumber);

            user.setOtp(resetOtp);
            user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            userRepository.save(user);

            emailService.sendPasswordResetEmail(request.getEmail(),resetOtp);

        }

        public void resetPassword(ResetPasswordRequest request){
            UserModel user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found."));

            // 2. Validate OTP
            if (user.getOtp() == null || !user.getOtp().equals(request.getOtp())) {
                throw new RuntimeException("Invalid OTP. Please try again.");
            }

            if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("OTP has expired. Please request a new one.");
            }

            // 3. Hash the NEW password and save it
            String hashedNewPassword = passwordEncoder.encode(request.getNewPassword());
            user.setPassword(hashedNewPassword);

            // 4. Security Cleanup: Erase the OTP so it can't be reused
            user.setOtp(null);
            user.setOtpExpiry(null);

            userRepository.save(user);
        }
    }
