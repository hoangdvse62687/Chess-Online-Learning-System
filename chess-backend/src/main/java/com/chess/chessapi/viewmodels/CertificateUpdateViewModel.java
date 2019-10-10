package com.chess.chessapi.viewmodels;


import org.hibernate.validator.constraints.Length;

public class CertificateUpdateViewModel {
    private long certificateId;

    @Length(max = 255,message = "Link should not larger than 255 characters")
    private String certificateLink;

    public long getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(long certificateId) {
        this.certificateId = certificateId;
    }

    public String getCertificateLink() {
        return certificateLink;
    }

    public void setCertificateLink(String certificateLink) {
        this.certificateLink = certificateLink;
    }
}
