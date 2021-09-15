package com.personal.dataretrievalsvc.service;

public class ServiceConstants {
        // Microsoft Vision OCR
        public static final String NON_TAX_DOCUMENTS_PATH = "classpath:inputCSV/inputNonTaxDocuments.csv";
        public static final String TAX_DOCUMENTS_PATH = "classpath:inputCSV/inputTaxDocuments.csv";

        public static final String RECOGNITION_RESULTS = "recognitionResults";
        public static final String REGIONS = "regions";
        public static final String LINES = "lines";
        public static final String WORDS = "words";
        public static final String TEXT = "text";

        public static final String PATTERN_PUNCTUATION = "[^a-zA-Z]+$";
        public static final String PATTERN_PUNCTUATION2 = "[^\\s]+$";

        // Google OCR Tesseract
        public static final String ENG_LANGUAGE = "eng";
        public static final String USER_DEFINED_DPI = "user_defined_dpi";
        public static final String USER_DEFINED_DPI_VALUE = "300";
        // mention a project tessdata folder path TESSDATA_PATH
        public static final String TESSDATA_PATH = "tessdata";
        public static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;

}
