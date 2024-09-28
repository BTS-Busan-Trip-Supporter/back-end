package org.bts.backend.dto.request;


public record ChangePWRequest(
        String oldPassword,
        String newPassword

){

}
