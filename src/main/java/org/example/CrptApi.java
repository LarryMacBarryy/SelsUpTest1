package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class CrptApi {

    private final HttpClient httpClient;
    private final String apiUrl = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private final int requestLimit;
    private final long intervalMillis;
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final ReentrantLock lock = new ReentrantLock();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.httpClient = HttpClient.newHttpClient();
        this.requestLimit = requestLimit;
        this.intervalMillis = timeUnit.toMillis(1);  // Convert interval to milliseconds

        // Schedule a task to reset the request count periodically
        scheduler.scheduleAtFixedRate(() -> requestCount.set(0), intervalMillis, intervalMillis, TimeUnit.MILLISECONDS);
    }

    public void createDocument(Document document, String signature) throws InterruptedException {
        lock.lock();
        try {
            // Wait if the request limit is exceeded
            while (requestCount.get() >= requestLimit) {
                lock.unlock();
                Thread.sleep(100);  // Wait before re-checking
                lock.lock();
            }

            // Increment the request count
            requestCount.incrementAndGet();

            // Convert the document to JSON
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(document);

            // Create and send the HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + signature)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Handle the response (for simplicity, we're just printing it here)
            System.out.println("Response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // Document class to represent the JSON payload
    public static class Document {
        private Description description;
        private String doc_id;
        private String doc_status;
        private String doc_type = "LP_INTRODUCE_GOODS";
        private boolean importRequest = true;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        private Product[] products;
        private String reg_date;
        private String reg_number;

        public void setDescription(Description description) {
            this.description = description;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public void setDoc_status(String doc_status) {
            this.doc_status = doc_status;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public void setParticipant_inn(String participant_inn) {
            this.participant_inn = participant_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }

        public void setProduction_date(String production_date) {
            this.production_date = production_date;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }

        public void setProducts(Product[] products) {
            this.products = products;
        }

        public void setReg_date(String reg_date) {
            this.reg_date = reg_date;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }

        public Description getDescription() {
            return description;
        }

        public String getDoc_id() {
            return doc_id;
        }

        public String getDoc_status() {
            return doc_status;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public boolean isImportRequest() {
            return importRequest;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public String getParticipant_inn() {
            return participant_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public String getProduction_date() {
            return production_date;
        }

        public String getProduction_type() {
            return production_type;
        }

        public Product[] getProducts() {
            return products;
        }

        public String getReg_date() {
            return reg_date;
        }

        public String getReg_number() {
            return reg_number;
        }
        // Getters and setters for all fields
        // ...

        public static class Description {
            private String participantInn;

            public String getParticipantInn() {
                return participantInn;
            }

            public void setParticipantInn(String participantInn) {
                this.participantInn = participantInn;
            }
        }

        public static class Product {
            private String certificate_document;
            private String certificate_document_date;
            private String certificate_document_number;
            private String owner_inn;
            private String producer_inn;
            private String production_date;
            private String tnved_code;
            private String uit_code;
            private String uitu_code;

            public void setCertificate_document(String certificate_document) {
                this.certificate_document = certificate_document;
            }

            public void setCertificate_document_date(String certificate_document_date) {
                this.certificate_document_date = certificate_document_date;
            }

            public void setCertificate_document_number(String certificate_document_number) {
                this.certificate_document_number = certificate_document_number;
            }

            public void setOwner_inn(String owner_inn) {
                this.owner_inn = owner_inn;
            }

            public void setProducer_inn(String producer_inn) {
                this.producer_inn = producer_inn;
            }

            public void setProduction_date(String production_date) {
                this.production_date = production_date;
            }

            public void setTnved_code(String tnved_code) {
                this.tnved_code = tnved_code;
            }

            public void setUit_code(String uit_code) {
                this.uit_code = uit_code;
            }

            public void setUitu_code(String uitu_code) {
                this.uitu_code = uitu_code;
            }

            public String getCertificate_document() {
                return certificate_document;
            }

            public String getCertificate_document_date() {
                return certificate_document_date;
            }

            public String getCertificate_document_number() {
                return certificate_document_number;
            }

            public String getOwner_inn() {
                return owner_inn;
            }

            public String getProducer_inn() {
                return producer_inn;
            }

            public String getProduction_date() {
                return production_date;
            }

            public String getTnved_code() {
                return tnved_code;
            }

            public String getUit_code() {
                return uit_code;
            }

            public String getUitu_code() {
                return uitu_code;
            }

            // Getters and setters for all fields
            // ...
        }
    }

}

