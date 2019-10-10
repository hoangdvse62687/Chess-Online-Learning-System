package com.chess.chessapi.services;

import com.chess.chessapi.constants.Common;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.security.UserPrincipal;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.viewmodels.LearnerStatusPublishCourseReportViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.math.BigInteger;
import java.util.List;

@Service
public class ReportService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userService;

    //PUBLIC METHOD DEFINED
    public PagedList<LearnerStatusPublishCourseReportViewModel> getLearnerStatusReport(int pageIndex, int pageSize,String courseName
            ,String sortBy,String sortDirection){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getLearnerStatusCourseReport");
        Common.storedProcedureQueryPaginationSetup(query,pageIndex,pageSize,sortBy,sortDirection);
        query.setParameter("authorId",userPrincipal.getId());
		query.setParameter("courseName",courseName);

        query.execute();
        final long totalElements = Long.parseLong(query.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(query.getResultList(),totalElements,pageSize);
    }

    public List<Integer> getEnrollmentReport(int year){
        UserPrincipal userPrincipal = this.userService.getCurrentUser();
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getEnrollmentReport");
        query.setParameter("authorId",userPrincipal.getId());
        query.setParameter("year", year);
        query.execute();

        return ManualCastUtils.castListObjectToListInteger(query.getResultList());
    }

    public List<Integer> getUsersRegisterReport(int year){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getUsersRegisterReport");
        query.setParameter("year", year);
        query.execute();

        return ManualCastUtils.castListObjectToListInteger(query.getResultList());
    }

    public List<Integer> getRateWinnableReport(int year){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getRateWinnableReport");
        query.setParameter("year", year);
        query.execute();

        return ManualCastUtils.castListObjectToListInteger(query.getResultList());
    }
    public List<Integer> getRateWinnableLevelReport(int year){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getRateWinnableLevelReport");
        query.setParameter("year", year);
        query.execute();

        return ManualCastUtils.castListObjectToListInteger(query.getResultList());
    }

    public List<BigInteger> getPublishCourseReport(){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getPublishCourseReport");
        query.execute();

        return query.getResultList();
    }

    public List<Integer> getCourseStatusReport(){
        StoredProcedureQuery query = this.em.createNamedStoredProcedureQuery("getCourseStatusReport");
        query.execute();

        return ManualCastUtils.castListObjectToListInteger(query.getResultList());
    }
    //END PUBLIC METHOD DEFINED

    //PRIVATE METHOD DEFINED
    private PagedList<LearnerStatusPublishCourseReportViewModel> fillDataToPaginationCustom(List<Object[]> rawData,long totalElements,int pageSize){
        long totalPages = (long) Math.ceil(totalElements / (double) pageSize);
        List<LearnerStatusPublishCourseReportViewModel> data = ManualCastUtils.castListObjectToLearnerStatusPublishCourseReport(rawData);
        return new PagedList<LearnerStatusPublishCourseReportViewModel>(Math.toIntExact(totalPages),totalElements,data);
    }
    //END PRIVATE METHOD DEFINED
}
