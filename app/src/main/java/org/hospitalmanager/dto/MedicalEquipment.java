package org.hospitalmanager.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class MedicalEquipment {
    static public class AddRequest {
        private String name;
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC+7")
        private Date latestMaintenanceDate;

        public String getName() {
            return name;
        }

        public Date getLatestMaintenanceDate() {
            return latestMaintenanceDate;
        }
    }

    static public class DispatchRequest {
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
        private String id;

        public String getId() {
            return id;
        }
    }
}
