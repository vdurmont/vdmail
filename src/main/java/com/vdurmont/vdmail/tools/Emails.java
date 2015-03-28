package com.vdurmont.vdmail.tools;

import com.vdurmont.vdmail.dto.Email;
import com.vdurmont.vdmail.exception.IllegalInputException;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;

import static com.vdurmont.vdmail.tools.Strings.isNullOrEmpty;

/**
 * Utility class for the emails
 */
public class Emails {
    public static List<String> validate(Email email) {
        List<String> errorFields = new ArrayList<>();
        if (email == null) {
            throw new IllegalInputException("Null email");
        } else {
            if (isNullOrEmpty(email.getContent())) errorFields.add("content");
            if (isNullOrEmpty(email.getSubject())) errorFields.add("subject");
            if (!EmailValidator.getInstance().isValid(email.getToAddress())) errorFields.add("toAddress");
        }
        return errorFields;
    }
}
