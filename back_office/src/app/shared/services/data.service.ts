import {Injectable} from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private data = new Subject<any>();
  data$ = this.data.asObservable();

  sendData(data: any) {
    this.data.next(data);
  }
  constructor() { }
}
