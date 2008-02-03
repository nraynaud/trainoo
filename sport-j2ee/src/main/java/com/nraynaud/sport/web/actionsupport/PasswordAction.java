package com.nraynaud.sport.web.actionsupport;

import com.nraynaud.sport.Application;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public abstract class PasswordAction extends DefaultAction {
    protected String password;
    protected String passwordConfirmation;
    public static final String PASSWORD_MAX_LENGTH = "20";
    public static final String PASSWORD_MIN_LENGTH = "5";

    public PasswordAction(final Application application) {
        super(application);
    }

    public String getPassword() {
        return password;
    }

    @StringLengthFieldValidator(
            message = "Votre mot de passe doit comporter entre "
                    + PASSWORD_MIN_LENGTH
                    + " et "
                    + PASSWORD_MAX_LENGTH
                    + " caractères.",
            minLength = PasswordAction.PASSWORD_MIN_LENGTH,
            maxLength = PasswordAction.PASSWORD_MAX_LENGTH)
    @RequiredStringValidator(message = "Le mot de passe est obligatoire.")
    public void setPassword(final String password) {
        this.password = password;
    }

    public void validate() {
        if (request.getMethod().equals("POST") && (passwordConfirmation == null || !passwordConfirmation.equals(
                password)))
            addFieldError("passwordConfirmation", "Le mot de passe et la confirmation doivent être identiques.");
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(final String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
