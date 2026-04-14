// ai-bpmn-chat.component.ts
import { Component, OnInit, AfterViewChecked, ElementRef, ViewChild, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { v4 as uuidv4 } from 'uuid';
import { AiBpmnService } from './ai-bpmn.service';
import { ChatMessage, ComplexityLevel, AiBpmnGenerateResponse, QualityAnalysis, ComplexityAnalysis } from './ai-bpmn.model';

@Component({
  selector: 'jhi-ai-bpmn-chat',
  templateUrl: './ai-bpmn-chat.component.html',
  styleUrls: ['./ai-bpmn-chat.component.scss']
})
export class AiBpmnChatComponent implements OnInit, AfterViewChecked {
  @ViewChild('chatContainer') chatContainer!: ElementRef;
  @ViewChild('inputTextarea') inputTextarea!: ElementRef;

  // ── UI State ─────────────────────────────────────────────
  messages: ChatMessage[] = [];
  inputText = '';
  isLoading = false;
  showSettings = false;

  // ── Settings ─────────────────────────────────────────────
  availableModels: string[] = [];
  selectedModel = 'deepseek-v3.1:671b-cloud';
  selectedComplexity: ComplexityLevel = 'auto';
  complexityOptions: { label: string; value: ComplexityLevel }[] = [
    { label: '🔍 Auto-detect', value: 'auto' },
    { label: '📋 Simple', value: 'simple' },
    { label: '⚙️ Medium', value: 'medium' },
    { label: '🔥 Complex', value: 'complex' }
  ];

  // ── Session ───────────────────────────────────────────────
  currentSessionId: string | null = null;
  currentXml: string | null = null;

  /** Flat conversation history for refine API */
  private conversationHistory: Array<{ role: string; content: string }> = [];

  private shouldScrollToBottom = false;

  constructor(private aiBpmnService: AiBpmnService, private router: Router, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadModels();
    this.pushWelcomeMessage();
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  // ── Init ─────────────────────────────────────────────────

  private loadModels(): void {
    this.aiBpmnService.getAvailableModels().subscribe({
      next: (models) => {
        this.availableModels = models;
        if (models.length > 0) this.selectedModel = models[0];
      },
      error: () => {
        this.availableModels = ['deepseek-v3.1:671b-cloud', 'minimax-m2.5:cloud', 'qwen3-coder:480b-cloud'];
      }
    });
  }

  private pushWelcomeMessage(): void {
    this.addMessage({
      role: 'assistant',
      type: 'text',
      content:
        '👋 Welcome to the **AI BPMN Generator**!\n\n' +
        "Describe your business process in plain language and I'll generate a Camunda-ready BPMN file for you.\n\n" +
        '_Example:_ "Order processing: receive order, validate, check inventory, process payment, ship, notify customer."'
    });
  }

  // ── Send ─────────────────────────────────────────────────

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.sendMessage();
    }
  }

  sendMessage(): void {
    const text = this.inputText.trim();
    if (!text || this.isLoading) return;

    this.inputText = '';
    this.addMessage({ role: 'user', type: 'text', content: text });
    this.conversationHistory.push({ role: 'user', content: text });

    if (this.currentSessionId && this.currentXml) {
      this.refine(text);
    } else {
      this.generate(text);
    }
  }

  private generate(description: string): void {
    this.isLoading = true;
    this.addLoadingMessage();

    this.aiBpmnService
      .generateBpmn({
        description,
        model: this.selectedModel,
        complexity: this.selectedComplexity === 'auto' ? undefined : this.selectedComplexity
      })
      .subscribe({
        next: (res) => this.handleResponse(res),
        error: (err) => this.handleError(err)
      });
  }

  private refine(prompt: string): void {
    this.isLoading = true;
    this.addLoadingMessage();

    this.aiBpmnService
      .refineBpmn({
        sessionId: this.currentSessionId!,
        refinementPrompt: prompt,
        currentXml: this.currentXml!,
        conversationHistory: this.conversationHistory,
        model: this.selectedModel
      })
      .subscribe({
        next: (res) => this.handleResponse(res),
        error: (err) => this.handleError(err)
      });
  }

  // ── Response Handling ─────────────────────────────────────

  private handleResponse(res: AiBpmnGenerateResponse): void {
    this.removeLoadingMessage();
    this.isLoading = false;

    if (!res || (!res.xml && !res.message)) {
      this.addMessage({
        role: 'assistant',
        type: 'error',
        content: '⚠️ The AI service returned an empty response. Please try again.'
      });
      return;
    }

    if (res.xml) {
      this.currentSessionId = res.sessionId;
      this.currentXml = res.xml;
      this.conversationHistory.push({ role: 'assistant', content: `Generated BPMN XML (${res.xml.length} chars)` });

      this.addMessage({
        role: 'assistant',
        type: 'bpmn',
        content: this.buildBpmnSummary(res),
        bpmnResult: res
      });
    } else {
      this.addMessage({
        role: 'assistant',
        type: 'error',
        content: `❌ ${res.message || 'Generation failed.'}`
      });
    }

    this.cdr.detectChanges();
  }

  private handleError(err: any): void {
    this.removeLoadingMessage();
    this.isLoading = false;
    this.addMessage({
      role: 'assistant',
      type: 'error',
      content: `❌ Request failed: ${err?.error?.message || err?.message || 'Unknown error'}`
    });
  }

  private buildBpmnSummary(res: AiBpmnGenerateResponse): string {
    const q = res.qualityAnalysis || ({} as QualityAnalysis);
    const c = (res.complexityAnalysis || {}) as ComplexityAnalysis;
    const parts: string[] = ['✅ BPMN generated successfully!'];
    if (c.level) parts.push(`**Complexity:** ${c.level} (score: ${c.score})`);
    if (q['task_count'] != null) parts.push(`**Tasks:** ${q['task_count']}`);
    if (q['event_count'] != null) parts.push(`**Events:** ${q['event_count']}`);
    if (q['gateway_count'] != null) parts.push(`**Gateways:** ${q['gateway_count']}`);
    if (!res.valid) parts.push('⚠️ XML has validation warnings — check errors below');
    parts.push('\n_Click the file card below to open it in the BPMN modeler._');
    return parts.join(' · ');
  }

  // ── Actions on BPMN result ────────────────────────────────

  openInModeler(res: AiBpmnGenerateResponse): void {
    // Store XML in sessionStorage, then navigate to bpmn-modeler
    sessionStorage.setItem('ai_bpmn_xml', res.xml);
    sessionStorage.setItem('ai_bpmn_session_id', res.sessionId);
    this.router.navigate(['/pages/process-management/process-management-bpmn/bpmn-modeler/add']);

  }

  deployBpmn(res: AiBpmnGenerateResponse): void {
    const name = `ai-generated-${Date.now()}`;
    this.aiBpmnService.deployBpmn(res.xml, name).subscribe({
      next: (deploymentId) => {
        this.addMessage({
          role: 'assistant',
          type: 'text',
          content: `🚀 Deployed to Camunda! Deployment ID: \`${deploymentId}\``
        });
      },
      error: (err) => {
        this.addMessage({
          role: 'assistant',
          type: 'error',
          content: `❌ Deploy failed: ${err?.error?.message || 'Unknown error'}`
        });
      }
    });
  }

  downloadBpmn(res: AiBpmnGenerateResponse): void {
    const blob = new Blob([res.xml], { type: 'application/xml' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `ai-bpmn-${Date.now()}.bpmn`;
    a.click();
    URL.revokeObjectURL(url);
  }

  startNewWorkflow(): void {
    this.currentSessionId = null;
    this.currentXml = null;
    this.conversationHistory = [];
    this.messages = [];
    this.pushWelcomeMessage();
  }

  // ── Helpers ───────────────────────────────────────────────

  private addMessage(partial: Omit<ChatMessage, 'id' | 'timestamp'>): void {
    this.messages.push({
      ...partial,
      id: uuidv4(),
      timestamp: new Date()
    });
    this.shouldScrollToBottom = true;
  }

  private addLoadingMessage(): void {
    this.messages.push({
      id: '__loading__',
      role: 'assistant',
      type: 'loading',
      content: '',
      timestamp: new Date()
    });
    this.shouldScrollToBottom = true;
  }

  private removeLoadingMessage(): void {
    this.messages = this.messages.filter((m) => m.id !== '__loading__');
  }

  private scrollToBottom(): void {
    try {
      const el = this.chatContainer?.nativeElement;
      if (el) el.scrollTop = el.scrollHeight;
    } catch {}
  }

  toggleSettings(): void {
    this.showSettings = !this.showSettings;
  }

  trackById(_: number, msg: ChatMessage): string {
    return msg.id;
  }
}
