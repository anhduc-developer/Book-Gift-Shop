package dev.anhduc.bookgiftshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.anhduc.bookgiftshop.dto.request.RequestChangePasswordDTO;
import dev.anhduc.bookgiftshop.dto.request.RequestLoginDTO;
import dev.anhduc.bookgiftshop.dto.response.ResLoginDTO;
import dev.anhduc.bookgiftshop.entity.User;
import dev.anhduc.bookgiftshop.exception.IdInvalidException;
import dev.anhduc.bookgiftshop.service.UserService;
import dev.anhduc.bookgiftshop.utils.SecurityUtil;
import dev.anhduc.bookgiftshop.utils.annotation.ApiMessage;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
            UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${duck.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    @PostMapping("/auth/login")
    @ApiMessage("User Login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody RequestLoginDTO loginDTO) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());
        // Xác thực người dùng => cần function loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        // chạy tới đây là chắc chắn user tồn tại
        User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
                    currentUserDB.getFullName());
            resLoginDTO.setUser(userLogin);
        }
        // create access token
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUser());
        resLoginDTO.setAccess_token(access_token);

        // create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), resLoginDTO);

        // update User
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

        // set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", refresh_token).httpOnly(true)
                .secure(true) // Cookie chỉ gửi qua HTTPS
                .path("/") // Cookie áp dụng cho toàn bộ domain /api => dùng được
                .maxAge(refreshTokenExpiration) // Thời gian sống của cookie
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);

    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch Account")
    public ResponseEntity<ResLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User currentUserDB = this.userService.handleGetUserByUsername(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (currentUserDB != null) {
            userLogin.setEmail(email);
            userLogin.setId(currentUserDB.getId());
            userLogin.setFullName(currentUserDB.getFullName());
            resLoginDTO.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userLogin);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User By Refresh Token")
    public ResponseEntity<ResLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", required = false) String refresh_token)
            throws IdInvalidException {
        if (refresh_token.equals("abc")) {
            throw new IdInvalidException("bạn không có refresh token ở cookie!");
        }
        // check validate
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);

        // check user by token + email => neu token hop le ma email khong hop le =>
        // reject
        String email = decodedToken.getSubject();
        this.userService.checkUserAndEmailValid(refresh_token, email);
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        User currentUserDB = this.userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(),
                    currentUserDB.getFullName());
            resLoginDTO.setUser(userLogin);
        }
        // create access token
        String access_token = this.securityUtil.createAccessToken(email, resLoginDTO.getUser());
        resLoginDTO.setAccess_token(access_token);

        // create refresh token
        String new_refresh_token = this.securityUtil.createRefreshToken(email, resLoginDTO);

        // update User
        this.userService.updateUserToken(new_refresh_token, email);

        // set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_token", new_refresh_token).httpOnly(true)
                .secure(true) // Cookie chỉ gửi qua HTTPS
                .path("/") // Cookie áp dụng cho toàn bộ domain /api => dùng được
                .maxAge(refreshTokenExpiration) // Thời gian sống của cookie
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).body(resLoginDTO);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Logout User")
    public ResponseEntity<Void> logout() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Access Token không hợp lệ");
        }
        // update refresh token = null
        this.userService.updateUserToken(null, email);
        // remove refresh token cookie
        ResponseCookie deleteSpringCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString()).body(null);
    }

    @PostMapping("/auth/change-password")
    @ApiMessage("Change Password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody RequestChangePasswordDTO dto)
            throws IdInvalidException {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                dto.getEmail(), dto.getPassword());
        // Xác thực người dùng => cần function loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        if (dto.getNewPassword().equals(dto.getPassword())) {
            throw new IdInvalidException("Mật khẩu mới phải khác mật khẩu cũ!");
        }
        dto.setNewPassword(this.passwordEncoder.encode(dto.getNewPassword()));
        this.userService.changePassword(dto);
        return ResponseEntity.ok().body("Change password successed!");
    }
}
