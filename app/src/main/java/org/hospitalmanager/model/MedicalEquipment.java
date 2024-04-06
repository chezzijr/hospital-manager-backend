package org.hospitalmanager.model;

import com.google.cloud.firestore.annotation.DocumentId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a medical equipment in the hospital.
 * The firestore collection name is "medicalEquipment".
 * The document id is the type of the medical equipment
 * which is unique for each type of medical equipment
 * and store array of medical equipment objects.
 */
public class MedicalEquipment {
    static public class Usage {
        private Date startDate;
        private Date endDate;
        // The faculty member who used the equipment
        private String assignedTo;

        public Usage() {
        }

        public Usage(Date startDate, Date endDate, String assignedTo) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.assignedTo = assignedTo;
        }

        public Date getStartDate() {
            return startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public String getAssignedTo() {
            return assignedTo;
        }
    }

    @DocumentId
    private String id;
    private String name;
    private boolean available;
    private Date latestUsageDate;
    private Date latestMaintenanceDate;
    private String isBeingUsedBy;
    // The date when the equipment was imported to the hospital
    private Date importedDate;
    private List<Usage> usageHistory;


    public MedicalEquipment() {
    }

    public MedicalEquipment(String name, Date lastMaintenanceDate) {
        this.name = name;
        this.available = true;
        this.latestMaintenanceDate = lastMaintenanceDate;
        this.latestUsageDate = null;
        this.isBeingUsedBy = null;
        this.importedDate = new Date();
        this.usageHistory = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public Date getLatestUsageDate() {
        return latestUsageDate;
    }

    public String getIsBeingUsedBy() {
        return isBeingUsedBy;
    }

    public Date getLatestMaintenanceDate() {
        return latestMaintenanceDate;
    }

    public Date getImportedDate() {
        return importedDate;
    }

    public List<Usage> getUsageHistory() {
        return usageHistory;
    }
}
