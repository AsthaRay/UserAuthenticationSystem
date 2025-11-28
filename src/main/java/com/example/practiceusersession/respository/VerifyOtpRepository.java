package com.example.practiceusersession.respository;

import com.example.practiceusersession.model.VerifyOtp;
import com.example.practiceusersession.model.enums.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerifyOtpRepository extends JpaRepository<VerifyOtp, Long> {

    @Query("SELECT v FROM VerifyOtp v WHERE v.email = :email AND v.type = :type AND (v.isExpired = false OR v.isUsed = false)")
    List<VerifyOtp> findInvalidOtpsByEmail(@Param("email") String email, @Param("type") OtpType type);

    Optional<VerifyOtp> findByOtpSessionId(String otpSessionId);

}
