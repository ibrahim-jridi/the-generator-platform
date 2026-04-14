// ai-bpmn.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import {
  AiBpmnGenerateRequest,
  AiBpmnGenerateResponse,
  AiBpmnRefineRequest
} from './ai-bpmn.model';
import { GlobalService } from '../../../shared/services/global.service';
import { map } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AiBpmnService {
  // Call Python Flask directly — bypasses Spring Boot + proxy timeout
  private readonly AI_URL = 'http://localhost:5050';
  // Keep Spring Boot URL only for deploy
  private readonly BASE_URL = '/api/v1/ai-bpmn';

  constructor(private http: HttpClient) {}

  getAvailableModels(): Observable<string[]> {
    return of(['qwen3-coder:480b-cloud', 'minimax-m2.5:cloud', 'deepseek-v3.1:671b-cloud']);
  }
  // getAvailableModels(): Observable<string[]> {
  //   return this.http.get<any>(`${this.AI_URL}/models`).pipe(map((res) => res.models));
  // }

  generateBpmn(request: AiBpmnGenerateRequest): Observable<AiBpmnGenerateResponse> {
    return this.http.post<AiBpmnGenerateResponse>(`${this.AI_URL}/generate`, request);
  }

  refineBpmn(request: AiBpmnRefineRequest): Observable<AiBpmnGenerateResponse> {
    return this.http.post<AiBpmnGenerateResponse>(`${this.AI_URL}/refine`, request);
  }

  deployBpmn(xml: string, name: string): Observable<string> {
    return this.http.post<string>(`${this.BASE_URL}/deploy`, { xml, name });
  }
}