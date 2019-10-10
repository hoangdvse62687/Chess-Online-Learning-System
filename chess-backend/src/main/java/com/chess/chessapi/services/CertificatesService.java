package com.chess.chessapi.services;

import com.chess.chessapi.entities.Certificate;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.repositories.CertificatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CertificatesService {
    @Autowired
    private CertificatesRepository certificatesRepository;

    //public method
    public List<Certificate> findAllByUserId(long userId){
        return this.certificatesRepository.findAllByUserId(userId);
    }

    public void updateCertifications(List<Certificate> oldCetificates, List<Certificate> newCetificates,long userId){
        if(oldCetificates == null || oldCetificates.isEmpty()){
            //add all
            for (Certificate newCetificate:
                    newCetificates) {
                User user = new User();
                user.setUserId(userId);
                newCetificate.setUser(user);
                this.certificatesRepository.save(newCetificate);
            }
        }else if(newCetificates != null && !newCetificates.isEmpty()){
            //check if new cetificate has already or not, if it not yet c=> create

            for (Certificate newCetificate:
                    newCetificates) {
                boolean isExist = false;
                for (Certificate oldCetificate:
                        oldCetificates) {
                    if(newCetificate.getCertificateLink().equals(oldCetificate.getCertificateLink())){
                        isExist = true;
                        oldCetificates.remove(oldCetificate);
                        break;
                    }
                }
                if(!isExist){
                    User user = new User();
                    user.setUserId(userId);
                    newCetificate.setUser(user);
                    this.certificatesRepository.save(newCetificate);
                }
            }
            //check old records should be deleted
            for (Certificate oldCetificate:
                    oldCetificates) {
                this.certificatesRepository.delete(oldCetificate);
            }
        }
        else{
            //delete all
            for (Certificate cetificate:
                    oldCetificates) {
                this.certificatesRepository.delete(cetificate);
            }
        }
    }

    public void create(String link,long userId){
        this.certificatesRepository.create(link,userId);
    }
    //end public method
}
