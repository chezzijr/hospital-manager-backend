package org.hospitalmanager.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;

public class MedicalEquipment {
    static public class AddRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private Date latestMaintenanceDate;

        public String getName() {
            return name;
        }

        public Date getLatestMaintenanceDate() {
            return latestMaintenanceDate;
        }
    }

    static public class DispatchRequest {
        @NotBlank(message = "Id is required")
        private String id;

        // nullable json field
        private String uid;

        public String getId() {
            return id;
        }

        public String getUid() {
            return uid;
        }
    }

    static public class ReturnRequest {
        @NotBlank(message = "Id is required")
        private String id;

        public String getId() {
            return id;
        }
    }
}
