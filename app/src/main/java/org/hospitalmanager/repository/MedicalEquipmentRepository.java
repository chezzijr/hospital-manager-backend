package org.hospitalmanager.repository;

import java.util.ArrayList;
import java.util.List;

import org.hospitalmanager.model.MedicalEquipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.api.client.util.Lists;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.common.util.concurrent.MoreExecutors;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface MedicalEquipmentRepository {
    /**
     * Add a new medical equipment to the database
     * @param me Medical Equipment object
     * @return ID of the newly added medical equipment
     * @throws Exception
     */
    public String addMedicalEquipment(MedicalEquipment me) throws Exception;
    public MedicalEquipment getMedicalEquipment(String id) throws Exception;
    public List<MedicalEquipment> getMedicalEquipmentList() throws Exception;
    public void updateFieldObject(String id, String field, Object value, Object... moreFieldsAndValues) throws Exception;
    public void deleteMedicalEquipment(String id) throws Exception;
}

@Repository
class MedicalEquipmentRepositoryImpl implements MedicalEquipmentRepository {
    static private final String COLLECTION_NAME = "medicalEquipment";

    Logger logger = LoggerFactory.getLogger(MedicalEquipmentRepositoryImpl.class);


    @Autowired
    private Firestore firestore;

    private CollectionReference collRef;

    @PostConstruct
    private void init() {
        collRef = firestore.collection(COLLECTION_NAME);
    }

    @Override
    public MedicalEquipment getMedicalEquipment(String id) throws Exception {
        DocumentReference docRef = collRef
            .document(id);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot doc = future.get();
        if (doc.exists()) {
            return doc.toObject(MedicalEquipment.class);
        } else {
            throw new Exception("Medical Equipment not found");
        }
    }

    @Override
    public List<MedicalEquipment> getMedicalEquipmentList() throws Exception {
        ApiFuture<QuerySnapshot> future = collRef.get();
        QuerySnapshot doc = future.get();
        ArrayList<MedicalEquipment> meList = Lists.newArrayList();
        if (!doc.isEmpty()) {
            doc.forEach((d) -> {
                meList.add(d.toObject(MedicalEquipment.class));
            });
        }
        return meList;
    }

    @Override
    public String addMedicalEquipment(MedicalEquipment me) throws Exception {
        var newDoc = collRef.document();
        ApiFuture<WriteResult> future = newDoc.set(me, SetOptions.merge());
        future.addListener(() -> {
            logger.info("Medical Equipment added: " + newDoc.getId());
        }, MoreExecutors.directExecutor());
        return newDoc.getId();
    }

    @Override
    public void updateFieldObject(String id, String field, Object value, Object... moreFieldsAndValues) throws Exception {
        DocumentReference docRef = collRef.document(id);
        ApiFuture<WriteResult> future = docRef.update(field, value, moreFieldsAndValues);
        future.get();
    }

    @Override
    public void deleteMedicalEquipment(String id) throws Exception {
        DocumentReference docRef = collRef.document(id);
        ApiFuture<WriteResult> future = docRef.delete();
        future.addListener(() -> {
            logger.info("Medical Equipment deleted: " + id);
        }, MoreExecutors.directExecutor());
    }
}
