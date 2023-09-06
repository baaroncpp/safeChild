package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.model.enums.*;
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

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "middle_name")
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "phone_number_2")
    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    @Column(name = "image_path")
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Column(name = "display_name")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    @Column(name = "birth_date")
    public Date  getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JoinColumn(name = "country_id", referencedColumnName = "id",insertable = true,updatable = true)
    @OneToOne(fetch = FetchType.LAZY)
    public TCountry getCountry() {
        return country;
    }

    public void setCountry(TCountry country) {
        this.country = country;
    }

    @Column(name = "identification")
    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(IdentificationType identificationType) {
        this.identificationType = identificationType;
    }

    @Column(name = "identification_number")
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    @Column(name = "identification_path")
    public String getIdentificationPath() {
        return identificationPath;
    }

    public void setIdentificationPath(String identificationPath) {
        this.identificationPath = identificationPath;
    }

    @Column(name = "non_verified_email")
    public boolean isNonVerifiedEmail() {
        return nonVerifiedEmail;
    }

    public void setNonVerifiedEmail(boolean nonVerifiedEmail) {
        this.nonVerifiedEmail = nonVerifiedEmail;
    }

    @Column(name = "non_verified_phone_number")
    public boolean isNonVerifiedPhoneNumber() {
        return nonVerifiedPhoneNumber;
    }

    public void setNonVerifiedPhoneNumber(boolean nonVerifiedPhoneNumber) {
        this.nonVerifiedPhoneNumber = nonVerifiedPhoneNumber;
    }

}
