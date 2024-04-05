package org.hospitalmanager.dto;

public class MedicalEquipment {
    static public class AddRequest {
        private String name;
        private String assignedTo;

        public String getName() {
            return name;
        }

        public String getAssignedTo() {
            return assignedTo;
        }
    }
}
