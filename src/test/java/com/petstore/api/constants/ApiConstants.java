package com.petstore.api.constants;

public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String PET = "/pet";
    public static final String PET_BY_ID = "/pet/{petId}";
    public static final String FIND_BY_STATUS = "/pet/findByStatus";
    public static final String FIND_BY_TAGS = "/pet/findByTags";

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static final int SUCCESS = 200;
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int VALIDATION_ERROR = 422;
}