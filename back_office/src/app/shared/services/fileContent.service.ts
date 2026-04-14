import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileContentService {
  private fileContentSubject = new BehaviorSubject<string | null>(null);
  fileContent$ = this.fileContentSubject.asObservable();

  public setFileContent(content: string): void {
    this.fileContentSubject.next(content);
  }

  public getFileContent(): string | null {
    return this.fileContentSubject.getValue();
  }
}
