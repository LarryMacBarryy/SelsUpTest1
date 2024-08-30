package org.example;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CrptApi api = new CrptApi(TimeUnit.MINUTES, 100);

        CrptApi.Document doc = new CrptApi.Document();
        doc.setDoc_id("doc123");
        doc.setDoc_status("NEW");
        doc.setOwner_inn("1234567890");
        doc.setParticipant_inn("0987654321");
        doc.setProducer_inn("1122334455");
        doc.setProduction_date("2024-08-30");
        doc.setReg_date("2024-08-30");
        doc.setReg_number("reg123");

        CrptApi.Document.Product product = new CrptApi.Document.Product();
        product.setCertificate_document("cert123");
        product.setCertificate_document_date("2024-08-30");
        product.setCertificate_document_number("certnum123");
        product.setOwner_inn("1234567890");
        product.setProducer_inn("1122334455");
        product.setProduction_date("2024-08-30");
        product.setTnved_code("tnved123");
        product.setUit_code("uit123");
        product.setUitu_code("uitu123");

        doc.setProducts(new CrptApi.Document.Product[]{product});

        api.createDocument(doc, "your_signature_here");
    }
}