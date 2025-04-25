package com.project.famaMenouApp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.project.famaMenouApp.config.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A user.
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    private String phoneNumber;

    @NotNull
    @Column(nullable = false)
    private boolean phoneActivated = false;

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;

    @Column(name = "position", nullable = true)
    private String position;

    @Column(name = "range_user", nullable = true)
    private Double rangeUser;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "jhi_user_authority",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    @Size(max = 256)
    @Column(name = "token_device", length = 256)
    private String tokenDevice;

    @Size(max = 6)
    private String otpCode;

    private long otpExpiryTimeMillis;

    private String StripeAccountId;
    private String StripeCustomerId;

    private boolean termsOfServiceAccepted = false;

    private boolean promotionAccepted = false;

    private boolean emailNotifications = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    // Lowercase the login before saving it in database
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getRangeUser() {
        return rangeUser;
    }

    public void setRangeUser(Double rangeUser) {
        this.rangeUser = rangeUser;
    }

    public String getTokenDevice() {
        return tokenDevice;
    }

    public void setTokenDevice(String tokenDevice) {
        this.tokenDevice = tokenDevice;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStripeCustomerId() {
        return StripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        StripeCustomerId = stripeCustomerId;
    }

    public boolean isPhoneActivated() {
        return phoneActivated;
    }

    public void setPhoneActivated(boolean phoneActivated) {
        this.phoneActivated = phoneActivated;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public long getOtpExpiryTimeMillis() {
        return otpExpiryTimeMillis;
    }

    public void setOtpExpiryTimeMillis(long otpExpiryTimeMillis) {
        this.otpExpiryTimeMillis = otpExpiryTimeMillis;
    }

    public boolean isTermsOfServiceAccepted() {
        return termsOfServiceAccepted;
    }

    public void setTermsOfServiceAccepted(boolean termsOfServiceAccepted) {
        this.termsOfServiceAccepted = termsOfServiceAccepted;
    }

    public boolean isPromotionAccepted() {
        return promotionAccepted;
    }

    public void setPromotionAccepted(boolean promotionAccepted) {
        this.promotionAccepted = promotionAccepted;
    }

    public String getStripeAccountId() {
        return StripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        StripeAccountId = stripeAccountId;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    // Method to check if OTP is valid and not expired
    public String isOtpValid(String otp) {
        if (otpCode == null || !otpCode.equals(otp)) {
            return "wrong otp";
        } else if (System.currentTimeMillis() >= otpExpiryTimeMillis) {
            return "expired";
        } else {
            return "valid";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", activated=" + activated +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneActivated=" + phoneActivated +
                ", langKey='" + langKey + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", activationKey='" + activationKey + '\'' +
                ", resetKey='" + resetKey + '\'' +
                ", resetDate=" + resetDate +
                ", position='" + position + '\'' +
                ", rangeUser=" + rangeUser +
                ", authorities=" + authorities +
                ", tokenDevice='" + tokenDevice + '\'' +
                ", otpCode='" + otpCode + '\'' +
                ", otpExpiryTimeMillis=" + otpExpiryTimeMillis +
                ", termsOfServiceAccepted=" + termsOfServiceAccepted +
                ", promotionAccepted=" + promotionAccepted +
                ", emailNotifications=" + emailNotifications +

                '}';
    }
}
