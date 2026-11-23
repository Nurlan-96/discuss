package com.discuss.auth.dto;

public class LoginResponse {
    private String accessToken;
    private String tokenType;
    private Long expiresIn;

    public LoginResponse(){
    }

    
      public String getAccessToken() {
        return accessToken;
      }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

      public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

      public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
