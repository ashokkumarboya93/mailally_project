package com.mailally.dashboard.mapper;

import com.mailally.dashboard.dto.DashboardActivityDto;
import com.mailally.dashboard.dto.DashboardQuickActionDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper helper generating Quick Actions metadata and Activity stream items.
 */
@Component
public class DashboardMapper {

    public List<DashboardQuickActionDto> buildQuickActions(String userRole) {
        List<DashboardQuickActionDto> list = new ArrayList<>();
        list.add(new DashboardQuickActionDto("create-campaign", "Create Campaign", "Design and launch email campaign", "/campaigns/create", "send", "MANAGER"));
        list.add(new DashboardQuickActionDto("import-contacts", "Import Contacts", "Add contacts or upload CSV/Excel", "/contacts/import", "users", "MANAGER"));
        list.add(new DashboardQuickActionDto("create-template", "Create Template", "Build HTML template with variables", "/templates/create", "file-text", "MANAGER"));
        list.add(new DashboardQuickActionDto("create-segment", "Create Segment", "Define target audience rules", "/segments/create", "filter", "MANAGER"));

        if ("ADMIN".equalsIgnoreCase(userRole)) {
            list.add(new DashboardQuickActionDto("invite-user", "Invite User", "Add team member to organization", "/users/invite", "user-plus", "ADMIN"));
            list.add(new DashboardQuickActionDto("upgrade-plan", "Upgrade Plan", "Manage subscription and credits", "/billing/upgrade", "zap", "ADMIN"));
        }

        return list;
    }
}
