package com.pfe.dto;

import java.util.List;
import java.util.Map;

// Public class - matches filename
public class AiBpmnGenerateRequestDTO {

    /** Natural-language description of the workflow */
    private String description;

    /** Ollama model name, e.g. "deepseek-v3.1:671b-cloud" */
    private String model;

    /**
     * Complexity override: "simple" | "medium" | "complex" | null (auto-detect)
     */
    private String complexity;

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getComplexity() { return complexity; }
    public void setComplexity(String complexity) { this.complexity = complexity; }
}

