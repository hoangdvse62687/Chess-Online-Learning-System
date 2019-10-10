package com.chess.chessapi.services;

import com.chess.chessapi.constants.Common;
import com.chess.chessapi.entities.PointLog;
import com.chess.chessapi.entities.User;
import com.chess.chessapi.models.PagedList;
import com.chess.chessapi.repositories.PointLogRepository;
import com.chess.chessapi.utils.ManualCastUtils;
import com.chess.chessapi.utils.TimeUtils;
import com.chess.chessapi.viewmodels.PointLogViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Service
public class PointLogService {
    @Autowired
    private PointLogRepository pointLogRepository;

    @PersistenceContext
    private EntityManager em;

    //PUBLIC METHOD DEFINED
    public PointLog create(String content,Integer point,long userId){
        PointLog pointLog = new PointLog();
        User user = new User();
        user.setUserId(userId);
        pointLog.setUser(user);
        pointLog.setContent(content);
        pointLog.setPoint(point);
        pointLog.setCreatedDate(TimeUtils.getCurrentTime());

        return this.pointLogRepository.save(pointLog);
    }

    public PagedList<PointLogViewModel> getPointLogPaginationByUserId(long userId,int pageIndex,int pageSize
            ,String sortBy,String sortDirection){
        StoredProcedureQuery storedProcedureQuery = this.em.createNamedStoredProcedureQuery("getPointLogPaginationByUserid");
        storedProcedureQuery.setParameter("userId",userId);
        Common.storedProcedureQueryPaginationSetup(storedProcedureQuery,pageIndex,pageSize,sortBy,sortDirection);

        storedProcedureQuery.execute();

        List<Object[]> rawData = storedProcedureQuery.getResultList();
        final long totalElements = Long.parseLong(storedProcedureQuery.getOutputParameterValue("totalElements").toString());
        return this.fillDataToPaginationCustom(rawData,totalElements,pageSize);
    }
    //END PUBLIC METHOD DEFINED

    //PRIVATE METHOD DEFINED
    private PagedList<PointLogViewModel> fillDataToPaginationCustom(List<Object[]> rawData,long totalElements,int pageSize){
        long totalPages = (long) Math.ceil(totalElements / (double) pageSize);
        List<PointLogViewModel> data = ManualCastUtils.castListObjectToPointLogPagination(rawData);
        return new PagedList<PointLogViewModel>(Math.toIntExact(totalPages),totalElements,data);
    }
    //END PRIVATE METHOD DEFINED
}
