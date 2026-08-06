package com.mailally.scheduler.mapper;

import com.mailally.scheduler.dto.SchedulerResponseDto;
import com.mailally.scheduler.entity.Scheduler;
import org.springframework.stereotype.Component;

/**
 * Manual mapper between Scheduler entities and DTOs.
 */
@Component
public class SchedulerMapper {

    public SchedulerResponseDto toSchedulerResponseDto(Scheduler scheduler) {
        if (scheduler == null) return null;
        return SchedulerResponseDto.builder()
                .id(scheduler.getId())
                .organizationId(scheduler.getOrganization() != null ? scheduler.getOrganization().getId() : null)
                .campaignId(scheduler.getCampaign() != null ? scheduler.getCampaign().getId() : null)
                .campaignName(scheduler.getCampaign() != null ? scheduler.getCampaign().getName() : null)
                .executionType(scheduler.getExecutionType())
                .status(scheduler.getStatus())
                .scheduledTime(scheduler.getScheduledTime())
                .executedTime(scheduler.getExecutedTime())
                .errorMessage(scheduler.getErrorMessage())
                .createdBy(scheduler.getCreatedBy())
                .updatedBy(scheduler.getUpdatedBy())
                .createdAt(scheduler.getCreatedAt())
                .updatedAt(scheduler.getUpdatedAt())
                .build();
    }
}
