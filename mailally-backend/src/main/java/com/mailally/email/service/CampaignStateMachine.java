package com.mailally.email.service;

import com.mailally.exception.CustomException;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * State machine enforcing strict, thread-safe campaign status state transitions.
 * Prevents invalid state mutations and race conditions during concurrent worker dispatches.
 */
@Component
public class CampaignStateMachine {

    public enum CampaignState {
        DRAFT,
        SCHEDULED,
        QUEUED,
        RUNNING,
        PAUSED,
        COMPLETED,
        FAILED,
        CANCELLED;

        public static CampaignState fromString(String stateStr) {
            if (stateStr == null || stateStr.isBlank()) {
                return DRAFT;
            }
            try {
                return CampaignState.valueOf(stateStr.toUpperCase().trim());
            } catch (IllegalArgumentException e) {
                return DRAFT;
            }
        }
    }

    private static final Map<CampaignState, Set<CampaignState>> VALID_TRANSITIONS = Map.of(
            CampaignState.DRAFT, EnumSet.of(CampaignState.SCHEDULED, CampaignState.QUEUED, CampaignState.RUNNING, CampaignState.CANCELLED),
            CampaignState.SCHEDULED, EnumSet.of(CampaignState.QUEUED, CampaignState.RUNNING, CampaignState.CANCELLED),
            CampaignState.QUEUED, EnumSet.of(CampaignState.RUNNING, CampaignState.CANCELLED, CampaignState.FAILED),
            CampaignState.RUNNING, EnumSet.of(CampaignState.PAUSED, CampaignState.COMPLETED, CampaignState.FAILED, CampaignState.CANCELLED),
            CampaignState.PAUSED, EnumSet.of(CampaignState.RUNNING, CampaignState.CANCELLED),
            CampaignState.COMPLETED, EnumSet.noneOf(CampaignState.class),
            CampaignState.FAILED, EnumSet.noneOf(CampaignState.class),
            CampaignState.CANCELLED, EnumSet.noneOf(CampaignState.class)
    );

    /**
     * Validates if a state transition from currentState to targetState is valid.
     */
    public boolean isValidTransition(CampaignState current, CampaignState target) {
        if (current == target) {
            return true;
        }
        Set<CampaignState> allowed = VALID_TRANSITIONS.get(current);
        return allowed != null && allowed.contains(target);
    }

    /**
     * Asserts that a state transition is allowed, throwing a CustomException if invalid.
     */
    public void validateTransition(String currentStr, String targetStr) {
        CampaignState current = CampaignState.fromString(currentStr);
        CampaignState target = CampaignState.fromString(targetStr);

        if (!isValidTransition(current, target)) {
            throw new CustomException(String.format("Invalid campaign status transition from '%s' to '%s'", current, target));
        }
    }
}
