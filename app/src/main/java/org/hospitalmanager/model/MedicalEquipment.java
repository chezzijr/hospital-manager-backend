package org.hospitalmanager.model;

import com.google.cloud.firestore.annotation.DocumentId;
import java.util.Date;
import java.util.UUID;

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
        private String assignedTo;

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
    private String assignedTo;
    private Date lastMaintenanceDate;
    private Usage[] usageHistory;

    public MedicalEquipment() {
    }

    public MedicalEquipment(String name, boolean available, String assignedTo, Date lastMaintenanceDate, int usageCount) {
        this.name = name;
        this.available = available;
        this.assignedTo = assignedTo;
        this.lastMaintenanceDate = lastMaintenanceDate;
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

    public String getAssignedTo() {
        return assignedTo;
    }

    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setId(String id) {
        this.id = id;
    }
}
