package greenbuildings.idp.entity;

import greenbuildings.commons.api.utils.CommonUtils;
import greenbuildings.commons.springfw.impl.entities.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_otp")
@Getter
@NoArgsConstructor
public class UserOTP extends AbstractBaseEntity {

    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "expired_time", nullable = false)
    private LocalDateTime expiredTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    public UserOTP(UserEntity user) {
        this.user = user;
        this.otpCode = CommonUtils.generateRandomOTP(6);
        this.expiredTime = LocalDateTime.now().plusMinutes(10);
    }

    public void updateOtp(UserEntity user) {
        this.user = user;
        this.otpCode = CommonUtils.generateRandomOTP(6);
        this.expiredTime = LocalDateTime.now().plusMinutes(10);
    }
    
    public void resetOTP() {
        otpCode = CommonUtils.generateRandomOTP(6);
        expiredTime = LocalDateTime.now().plusMinutes(10);
    }

}
