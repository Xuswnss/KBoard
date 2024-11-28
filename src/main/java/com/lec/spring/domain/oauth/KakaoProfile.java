package com.lec.spring.domain.oauth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;


@Data
public class KakaoProfile {

    @JsonProperty("id")
    public Long id;
    @JsonProperty("connected_at")
    public String connectedAt;
    @JsonProperty("properties")
    public Properties properties;
    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @Data
    public class KakaoAccount {

        @JsonProperty("profile_nickname_needs_agreement")
        public Boolean profileNicknameNeedsAgreement;
        @JsonProperty("profile")
        public Profile profile;

        @Data
        public class Profile {
            @JsonProperty("nickname")
            public String nickname;
            @JsonProperty("is_default_nickname")
            public Boolean isDefaultNickname;

        }
    }


    public class Properties {

        @JsonProperty("nickname")
        public String nickname;

    }


}


