package co.edu.uniquindio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {

    private String token;

    public TokenDto(String token) {
        this.token = token;
    }
}