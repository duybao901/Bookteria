package com.bookteria.shared;

import lombok.Data;

@Data
public class Error {
    public String Message;
    public String Code;
    public Error(String message, String code)
    {
        Message = message;
        Code = code;
    }

    public static final Error NONE = new Error("", "");
}
