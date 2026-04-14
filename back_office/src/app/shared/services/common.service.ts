import {EventEmitter, Injectable, Output} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  @Output() validateTask = new EventEmitter<string>();

  constructor() {
  }

  validateTaskEmit() {
    this.validateTask.emit();
  }

}
