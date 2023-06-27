package com.indra.InQ.service;

import com.indra.InQ.modal.QueueModal;
import com.indra.InQ.modal.common.QueueDescription;
import com.indra.InQ.repository.QueueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    @Autowired
    QueueRepo queueRepo;
    public QueueModal createNewQueue(QueueDescription queueDescription){
        QueueModal queueModal=new QueueModal();
        queueModal.setName(queueDescription.getName());
        queueModal.setId(queueDescription.getId());
        queueModal.setStartRange(queueDescription.getStartRange());
        queueModal.setEndRange(queueDescription.getEndRange());
        queueModal.setMaxLimit(queueDescription.getMaxLimit());
        queueRepo.save(queueModal);
        return queueModal;
    }
}
