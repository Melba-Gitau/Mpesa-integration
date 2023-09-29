package com.example.jwt.Controller;

import com.example.jwt.Models.JWTRequest;
import com.example.jwt.Models.JWTResponse;
import com.example.jwt.Service.UserService;
import com.example.jwt.Utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        return "Welcome";
    }
//method to create jwt token once u pass in the userDetails.
    @PostMapping("/authenticate")
    public JWTResponse authentication(@RequestBody JWTRequest jwtRequest) throws Exception {

        //to authenticate details in SB web security
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                )
        );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID CREDENTIALS", e);
        }
        //getting user details
        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getUsername());

        //generate the token
        final String token
                = jwtUtility.generateToken(userDetails);

        return new JWTResponse(token);
    }
}
