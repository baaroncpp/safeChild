package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.model.enums.*;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 10/06/2022
 */
@Entity
@Table(name = "t_user_meta", schema = "core")
@ToString
@Setter
public class TUserMeta extends AuditEntity {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phoneNumber;
    private String phoneNumber2;
    private String imagePath;
    private String displayName;
    private GenderEnum gender;
    private Date birthDate;
    private String email;
    private TCountry country;
    private IdentificationType identificationType;
    private String identificationNumber;
    private String identificationPath;
    private Boolean nonVerifiedEmail;
    private Boolean nonVerifiedPhoneNumber;
    private String pin;
    private String physicalAddress;

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @Column(name = "middle_name")
    public String getMiddleName() {
        return middleName;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Column(name = "phone_number_2")
    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    @Column(name = "image_path")
    public String getImagePath() {
        return imagePath;
    }

    @Column(name = "display_name")
    public String getDisplayName() {
        return displayName;
    }

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    public GenderEnum getGender() {
        return gender;
    }

    @Column(name = "birth_date")
    public Date  getBirthDate() {
        return birthDate;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @JoinColumn(name = "country_id", referencedColumnName = "id",insertable = true,updatable = true)
    @OneToOne(fetch = FetchType.LAZY)
    public TCountry getCountry() {
        return country;
    }

    @Column(name = "identification")
    @Enumerated(EnumType.STRING)
    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    @Column(name = "identification_number")
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    @Column(name = "identification_path")
    public String getIdentificationPath() {
        return identificationPath;
    }

    @Column(name = "non_verified_email")
    public boolean isNonVerifiedEmail() {
        return nonVerifiedEmail;
    }

    @Column(name = "non_verified_phone_number")
    public boolean isNonVerifiedPhoneNumber() {
        return nonVerifiedPhoneNumber;
    }

    @Column(name = "pin")
    public String getPin() {
        return pin;
    }

    @Column(name = "physical_address")
    public String getPhysicalAddress() {
        return physicalAddress;
    }
}