package com.vdurmont.vdmail.tools;

import com.vdurmont.vdmail.exception.IllegalInputException;
import com.vdurmont.vdmail.model.Email;
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
            if (email.getSender() == null) {
                errorFields.add("sender");
            } else if (isNullOrEmpty(email.getSender().getAddress()) ||
                    !EmailValidator.getInstance().isValid(email.getSender().getAddress())) {
                errorFields.add("sender_address");
            }
            if (email.getRecipient() == null) {
                errorFields.add("recipient");
            } else if (isNullOrEmpty(email.getRecipient().getAddress()) ||
                    !EmailValidator.getInstance().isValid(email.getRecipient().getAddress())) {
                errorFields.add("recipient_address");
            }
            if (isNullOrEmpty(email.getContent())) {
                errorFields.add("content");
            }
            if (isNullOrEmpty(email.getSubject())) {
                errorFields.add("subject");
            }
        }
        return errorFields;
    }

    public static String clean(String address) {
        if (isNullOrEmpty(address) || !EmailValidator.getInstance().isValid(address)) {
            throw new IllegalInputException("Invalid address: " + address);
        }
        return address.trim();
    }
}
