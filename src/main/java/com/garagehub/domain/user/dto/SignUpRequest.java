package com.garagehub.domain.user.dto;

import com.garagehub.domain.user.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotBlank
    @Pattern(regexp = "^[a-z][a-z0-9]{3,19}$", 
             message = "아이디는 영문 소문자로 시작하고 영문 소문자, 숫자만 사용 가능합니다. (4~20자)")
    private String username;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
             message = "비밀번호는 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
    private String password;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,100}$",
             message = "이름은 한글 또는 영문만 사용 가능합니다. (2~100자)")
    private String name;

    @NotBlank
    @Pattern(regexp = "^01[0-9]{8,9}$", 
             message = "올바른 전화번호 형식이 아닙니다.")
    private String phone;

    @NotNull(message = "생년월일을 입력해주세요.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birthDate;

    @NotNull(message = "성별을 입력해주세요.")
    private Gender gender;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 255, message = "이메일은 255자 이하여야 합니다.")
    private String email;
}