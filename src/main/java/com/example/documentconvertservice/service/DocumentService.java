package com.example.documentconvertservice.service;

import com.example.documentconvertservice.dto.DocumentDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DocumentService {

    /** Map of paris {}username : documents} */
    private static final HashMap<String, List<DocumentDTO>> DOCUMENT_GROUP = new HashMap<>();


    public List<DocumentDTO> getDocuments(String username) {
        List<DocumentDTO> documents = DOCUMENT_GROUP.get(username);

        if (documents == null) {
            documents = new ArrayList<>();
            addNewDocumentGroup(username, documents);
        }

        return documents;
    }

    public List<DocumentDTO> addNewDocumentGroup(String username, List<DocumentDTO> documents) {
        return DOCUMENT_GROUP.put(username, documents);
    }

    public void deleteGroup(String username) {
        DOCUMENT_GROUP.remove(username);
    }
}
