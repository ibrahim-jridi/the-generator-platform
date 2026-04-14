import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedDataService {
  private formDataSource = new BehaviorSubject<string>(null);

  constructor() { }

  changeFormData(data: string) {
    this.formDataSource.next(data);
  }
}
