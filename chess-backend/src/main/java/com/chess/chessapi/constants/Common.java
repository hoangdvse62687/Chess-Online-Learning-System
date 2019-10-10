package com.chess.chessapi.constants;

import javax.persistence.StoredProcedureQuery;

public class Common {
    public static StoredProcedureQuery storedProcedureQueryPaginationSetup(StoredProcedureQuery storedProcedureQuery
            ,int pageIndex,int pageSize,String sortBy,String sortDirection){
        if(storedProcedureQuery != null){
            storedProcedureQuery.setParameter("pageIndex",(pageIndex - 1) * pageSize);
            storedProcedureQuery.setParameter("pageSize",pageSize);
            storedProcedureQuery.setParameter("sortBy",sortBy);
            storedProcedureQuery.setParameter("sortDirection",sortDirection);
            storedProcedureQuery.setParameter("totalElements",Long.parseLong("0"));
        }
        return storedProcedureQuery;
    }
}
