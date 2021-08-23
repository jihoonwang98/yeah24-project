package com.prac.domain.etc;

import lombok.*;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Address {

    @NotBlank(message = "우편번호를 입력해야 합니다.")
    private String postcode;  // 우편번호

    @NotBlank(message = "도로명주소를 입력해야 합니다.")
    private String roadAddr;  // 도로명주소

    @NotBlank(message = "지번주소를 입력해야 합니다.")
    private String jibunAddr; // 지번주소

    @NotBlank(message = "상세주소를 입력해야 합니다.")
    private String detailAddr;  // 상세주소

    @NotBlank(message = "참고항목을 입력해야 합니다.")
    private String extraAddr; // 참고항목

    public String fullRoadAddress() {
        return "(" + postcode + ") " + roadAddr + "," + detailAddr + " " + extraAddr;
    }
}
