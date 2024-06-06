package com.bwongo.core.core_banking.service;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.core_banking.model.dto.DefaultRegisterUserDto;
import com.bwongo.core.core_banking.model.dto.DefaultUpdateUserDto;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.student_mgt.model.jpa.TGuardian;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.user_mgt.model.jpa.TUserMeta;
import lombok.extern.slf4j.Slf4j;
import nl.strohalm.cyclos.webservices.members.MemberRegistrationResult;
import nl.strohalm.cyclos.webservices.members.RegisterMemberParameters;
import nl.strohalm.cyclos.webservices.members.UpdateMemberParameters;
import nl.strohalm.cyclos.webservices.model.MemberVO;
import nl.strohalm.cyclos.webservices.model.RegistrationFieldValueVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.bwongo.core.core_banking.utils.CoreBankingMsgConstant.*;
import static com.bwongo.core.core_banking.utils.CoreBankingWebServiceUtils.getWebServiceFactory;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/3/23
 **/
@Service
@Slf4j
public class MemberService {

    @Value("${core-banking.group-id.school}")
    private Long schoolGroupId;

    @Value("${core-banking.group-id.student}")
    private Long studentGroupId;

    @Value("${core-banking.group-id.driver}")
    private Long driverGroupId;

    @Value("${core-banking.group-id.school-staff}")
    private Long schoolStaffGroupId;

    public Long addSchoolToCoreBanking(TSchool school){

        List<RegistrationFieldValueVO> customFields = Arrays.asList(
                new RegistrationFieldValueVO("receiver_phone", school.getPhoneNumber()),
                new RegistrationFieldValueVO("category", school.getSchoolCategory().getNote()),
                new RegistrationFieldValueVO("sms_amount", school.getSmsCost().toString()),
                new RegistrationFieldValueVO("std_school", school.getSchoolName()),
                new RegistrationFieldValueVO("address", school.getDistrict().getName() +", "+school.getPhysicalAddress())
        );

        var coreBankingSchool = new DefaultRegisterUserDto(
                school.getSchoolName(),
                school.getAccountNumber(),
                "2023",
                school.getEmail(),
                schoolGroupId,
                customFields
        );

        return defaultUserRegistry(coreBankingSchool);
    }

    public void updateSchoolToCoreBanking(Long id, TSchool school){

        List<RegistrationFieldValueVO> customFields = Arrays.asList(
                new RegistrationFieldValueVO("receiver_phone", school.getPhoneNumber()),
                new RegistrationFieldValueVO("category", school.getSchoolCategory().getNote()),
                new RegistrationFieldValueVO("sms_amount", school.getSmsCost().toString()),
                new RegistrationFieldValueVO("std_school", school.getSchoolName()),
                new RegistrationFieldValueVO("address", school.getDistrict().getName() +", "+school.getPhysicalAddress())
        );

        var coreBankingSchool = new DefaultUpdateUserDto(
                school.getSchoolName(),
                school.getAccountNumber(),
                school.getEmail(),
                customFields
        );

        updateCoreBankingUser(id, coreBankingSchool);
    }

    public Long addSchoolStaff(TUserMeta userMeta, TSchool school, String username){

        List<RegistrationFieldValueVO> customFields = Arrays.asList(
                new RegistrationFieldValueVO("receiver_phone", userMeta.getPhoneNumber()),
                new RegistrationFieldValueVO("std_school", school.getSchoolName())
                //new RegistrationFieldValueVO("school_id", school.getCoreBankingId().toString())
        );

        var coreBankingSchool = new DefaultRegisterUserDto(
                userMeta.getLastName()+" "+userMeta.getFirstName(),
                username,
                "5665",
                userMeta.getEmail(),
                schoolStaffGroupId,
                customFields
        );
        return defaultUserRegistry(coreBankingSchool);
    }

    public void updateSchoolStaff(Long id, TUserMeta userMeta, TSchool school, String username){

        List<RegistrationFieldValueVO> customFields = Arrays.asList(
                new RegistrationFieldValueVO("receiver_phone", userMeta.getPhoneNumber()),
                new RegistrationFieldValueVO("std_school", school.getSchoolName())
                //new RegistrationFieldValueVO("school_id", school.getCoreBankingId().toString())
        );

        var coreBankingSchool = new DefaultUpdateUserDto(
                userMeta.getLastName()+" "+userMeta.getFirstName(),
                username,
                userMeta.getEmail(),
                customFields
        );

        updateCoreBankingUser(id, coreBankingSchool);
    }

    public Long addSchoolDriver(TUserMeta userMeta, TSchool school, String username){

        List<RegistrationFieldValueVO> customFields = Arrays.asList(
                new RegistrationFieldValueVO("receiver_phone", userMeta.getPhoneNumber()),
                new RegistrationFieldValueVO("std_school", school.getSchoolName()),
                new RegistrationFieldValueVO("sms_cost", school.getSmsCost().toString()),
                new RegistrationFieldValueVO("address", userMeta.getPhysicalAddress())
                //new RegistrationFieldValueVO("school_id", school.getCoreBankingId().toString())
        );

        var coreBankingSchool = new DefaultRegisterUserDto(
                userMeta.getLastName()+" "+userMeta.getFirstName(),
                username,
                "5665",
                userMeta.getEmail(),
                driverGroupId,
                customFields
        );
        return defaultUserRegistry(coreBankingSchool);
    }

    public void updateSchoolDriver(Long id, TUserMeta userMeta, TSchool school, String username){

        List<RegistrationFieldValueVO> customFields = Arrays.asList(
                new RegistrationFieldValueVO("receiver_phone", userMeta.getPhoneNumber()),
                new RegistrationFieldValueVO("std_school", school.getSchoolName()),
                new RegistrationFieldValueVO("sms_cost", school.getSmsCost().toString()),
                new RegistrationFieldValueVO("address", userMeta.getPhysicalAddress())
                //new RegistrationFieldValueVO("school_id", school.getCoreBankingId().toString())
        );

        var coreBankingSchool = new DefaultUpdateUserDto(
                userMeta.getLastName()+" "+userMeta.getFirstName(),
                username,
                userMeta.getEmail(),
                customFields
        );

        updateCoreBankingUser(id, coreBankingSchool);
    }

    public Long addStudent(TStudent student, TGuardian guardian){

        List<RegistrationFieldValueVO> customFields = Arrays.asList(
                new RegistrationFieldValueVO("receiver_phone", guardian.getPhoneNumber()),
                new RegistrationFieldValueVO("std_school", student.getSchool().getSchoolName()),
                //new RegistrationFieldValueVO("sms_cost", sms),
                new RegistrationFieldValueVO("address", student.getPhysicalAddress()),
                new RegistrationFieldValueVO("std_class", student.getStudentClass()),
                new RegistrationFieldValueVO("school_account", student.getSchool().getAccountNumber()),
                new RegistrationFieldValueVO("parent", guardian.getFullName()),
                new RegistrationFieldValueVO("school_id", student.getSchool().getUsername())
        );

        var coreBankingStudent = new DefaultRegisterUserDto(
                student.getFirstName() +" "+ student.getSecondName(),
                student.getStudentUsername(),
                "2023",
                student.getEmail(),
                studentGroupId,
                customFields
        );
        return defaultUserRegistry(coreBankingStudent);
    }

    public void updateStudent(Long id, TStudent student, TGuardian guardian) {

        List<RegistrationFieldValueVO> customFields = Arrays.asList(
                new RegistrationFieldValueVO("receiver_phone", guardian.getPhoneNumber()),
                new RegistrationFieldValueVO("std_school", student.getSchool().getSchoolName()),
                new RegistrationFieldValueVO("sms_cost", "0.0"),
                new RegistrationFieldValueVO("address", student.getPhysicalAddress()),
                new RegistrationFieldValueVO("std_class", student.getStudentClass()),
                new RegistrationFieldValueVO("school_account", student.getSchool().getUsername()),
                new RegistrationFieldValueVO("parent", guardian.getFullName())
                //new RegistrationFieldValueVO("school_id", student.getSchool().getCoreBankingId().toString())
        );

        var studentCoreBanking = new DefaultUpdateUserDto(
                student.getFirstName() +" "+ student.getSecondName(),
                student.getFirstName(),
                student.getEmail(),
                customFields
        );

        updateCoreBankingUser(id, studentCoreBanking);
    }

    private Long defaultUserRegistry(DefaultRegisterUserDto defaultRegisterUserDto){
        var memberParameters = new RegisterMemberParameters();
        memberParameters.setGroupId(defaultRegisterUserDto.groupId());
        memberParameters.setPin(defaultRegisterUserDto.pin());
        memberParameters.setUsername(defaultRegisterUserDto.username());
        memberParameters.setName(defaultRegisterUserDto.fullName());
        memberParameters.setEmail(defaultRegisterUserDto.email());
        memberParameters.setFields(defaultRegisterUserDto.customFields());

        var memberWebService = getWebServiceFactory().getMemberWebService();

        MemberRegistrationResult result = null;
        try {
            result = memberWebService.registerMember(memberParameters);
        } catch (Exception e) {
            var errorMsg = e.getMessage();
            Validate.filterException(this, errorMsg.substring(errorMsg.lastIndexOf(":") + 1));
        }

        Validate.notNull(this, result, ExceptionType.BAD_REQUEST, USER_REG_FAILED);

        if(result != null)
            log.info(String.format("%s successfully registered", result.getUsername()));

        return result.getId();
    }

    private void updateCoreBankingUser(Long id, DefaultUpdateUserDto defaultUpdateUserDto){

        var memberParameters = new UpdateMemberParameters();
        //memberParameters.setId(id);
        memberParameters.setEmail(defaultUpdateUserDto.email());
        memberParameters.setName(defaultUpdateUserDto.fullName());
        memberParameters.setPrincipalType("USER");
        memberParameters.setPrincipal(defaultUpdateUserDto.username());
        memberParameters.setFields(defaultUpdateUserDto.customFields());

        var memberWebService = getWebServiceFactory().getMemberWebService();

        try {
            memberWebService.updateMember(memberParameters);
        } catch (Exception e) {
            var errorMsg = e.getMessage();
            log.info(errorMsg);
            Validate.filterException(this, errorMsg.substring(errorMsg.lastIndexOf(":") + 1));
        }
    }

    public MemberVO getUserByUsername(String username){
        var memberWebService = getWebServiceFactory().getMemberWebService();

        MemberVO result = null;
        try {
            result = memberWebService.loadByUsername(username);
        }catch(Exception e){
            var errorMsg = e.getMessage();
            Validate.filterException(this, errorMsg.substring(errorMsg.lastIndexOf(":") + 1));
        }
        Validate.notNull(this, result, ExceptionType.RESOURCE_NOT_FOUND, "Not found", username);

        return result;
    }
}
