package com.pfe.validator.impl;

import com.pfe.validator.IMinioValidator;
import com.pfe.web.rest.errors.Reason;
import com.pfe.web.rest.errors.ValidationException;
import io.minio.MinioClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component("minioValidator")
public class MinioValidatorImpl implements IMinioValidator {

    private static final String NAME_PATTERN = "Invalid name pattern";
    private static final String NAME_REQUIRED = "Le nom de bucket est obligatoire";

    private static final String BUCKET_NAME_REGEX = "^(?=.{3,63}$)(?!xn--)(?!.*\\.-)(?!.*-\\.)(?!.*\\.\\.)[a-z0-9.-]+(?<!-s3alias)$";

    private final Pattern namePattern;

    public MinioValidatorImpl() {
        this.namePattern = Pattern.compile(BUCKET_NAME_REGEX);
    }

    @Override
    public void beforeSave(String bucketName) {
        List<Reason> reasons = new ArrayList<>();

        if (bucketName == null || bucketName.isBlank()) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.NAME_REQUIRED, NAME_REQUIRED));
        } else if (!validatePattern(namePattern, bucketName)) {
            reasons.add(new Reason<>(ValidationException.ReasonCode.NAME_PATTERN, NAME_PATTERN));
        }

        if (!reasons.isEmpty()) {
            throw new ValidationException(
                reasons.stream()
                    .map(reason -> String.valueOf(reason.code))
                    .collect(Collectors.joining(", "))
            );
        }
    }

    private boolean validatePattern(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    @Override
    public void beforeSave(MinioClient minioClient) {
        // Implementation for save validation with MinioClient
    }
}
