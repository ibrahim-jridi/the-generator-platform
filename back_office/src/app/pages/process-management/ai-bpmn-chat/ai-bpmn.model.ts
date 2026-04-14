// ai-bpmn.model.ts
// Models for the AI BPMN Chat Generator component

export type ComplexityLevel = 'simple' | 'medium' | 'complex' | 'auto';

export type MessageRole = 'user' | 'assistant' | 'system';

export type MessageType = 'text' | 'bpmn' | 'error' | 'loading';

export interface ChatMessage {
  id: string;
  role: MessageRole;
  type: MessageType;
  content: string;
  timestamp: Date;
  /** Present when type === 'bpmn' */
  bpmnResult?: AiBpmnGenerateResponse;
}

export interface AiBpmnGenerateRequest {
  description: string;
  model: string;
  complexity?: string;
}

export interface AiBpmnRefineRequest {
  sessionId: string;
  refinementPrompt: string;
  currentXml: string;
  conversationHistory: Array<{ role: string; content: string }>;
  model: string;
}

export interface QualityAnalysis {
  task_count?: number;
  event_count?: number;
  gateway_count?: number;
  [key: string]: any;
}

export interface ComplexityAnalysis {
  level: ComplexityLevel;
  score: number;
}

export interface AiBpmnGenerateResponse {
  sessionId: string;
  xml: string;
  valid: boolean;
  errors: string[];
  warnings: string[];
  qualityAnalysis: QualityAnalysis;
  complexityAnalysis: ComplexityAnalysis;
  message: string;
}
