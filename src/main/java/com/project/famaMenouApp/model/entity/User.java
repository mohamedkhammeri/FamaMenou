package com.project.famaMenouApp.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.famaMenouApp.config.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"password", "activationKey", "resetKey", "authorities", "shops"}) // Exclude sensitive fields
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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
    @Builder.Default
    private boolean activated = false;

    private String phoneNumber;

    @NotNull
    @Column(nullable = false)
    @Builder.Default
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
    private Instant resetDate;

    @Column(name = "position")
    private String position;

    @Column(name = "range_user")
    private Double rangeUser;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "jhi_user_authority",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_name")
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    @Size(max = 256)
    @Column(name = "token_device", length = 256)
    private String tokenDevice;

    @Size(max = 6)
    private String otpCode;

    private long otpExpiryTimeMillis;

    private String stripeAccountId;
    private String stripeCustomerId;

    @Builder.Default
    private boolean termsOfServiceAccepted = false;

    @Builder.Default
    private boolean promotionAccepted = false;

    @Builder.Default
    private boolean emailNotifications = true;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Shop> shops = new HashSet<>();

    // Custom method remains as it contains logic
    public String isOtpValid(String otp) {
        if (otpCode == null || !otpCode.equals(otp)) {
            return "wrong otp";
        } else if (System.currentTimeMillis() >= otpExpiryTimeMillis) {
            return "expired";
        } else {
            return "valid";
        }
    }

    // Custom setter for login to ensure lowercase
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    // Keep equals and hashCode consistent with JPA requirements
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}