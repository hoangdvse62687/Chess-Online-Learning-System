package com.chess.chessapi.jobs;

import com.chess.chessapi.services.SuggestionAlgorithmService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class CourseSuggestionJob implements Job {

    @Autowired
    private SuggestionAlgorithmService suggestionAlgorithmService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if(this.suggestionAlgorithmService != null){
            this.suggestionAlgorithmService.executeUserFilterSuggestionAlgorithm();
            this.suggestionAlgorithmService.executeItemFilterSuggestionAlgorithm();
            this.suggestionAlgorithmService.executeCommonItemFilterSuggestionAlgorithm();
        }
    }
}
