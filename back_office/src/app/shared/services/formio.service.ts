import {options} from '../../../assets/formio/options';
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class FormioService {

  constructor() {
  }

  getFormioOptions(): any {
    return options
  }

}
