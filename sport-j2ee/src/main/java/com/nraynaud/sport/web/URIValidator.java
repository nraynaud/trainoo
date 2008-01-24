package com.nraynaud.sport.web;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

import java.net.URI;
import java.net.URISyntaxException;

public class URIValidator extends FieldValidatorSupport {
    public void validate(final Object object) throws ValidationException {
        final String fieldName = getFieldName();
        final Object value = this.getFieldValue(fieldName, object);

        // if there is no value - don't do comparison
        // if a value is required, a required validator should be added to the field
        if (value == null || value.toString().length() == 0) {
            return;
        }

        if (!(value.getClass().equals(String.class)) || !verifyUrI((String) value)) {
            addFieldError(fieldName, object);
        }
    }

    private static boolean verifyUrI(final String candidate) {
        try {
            return "http".equals(new URI(candidate).getScheme());
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
