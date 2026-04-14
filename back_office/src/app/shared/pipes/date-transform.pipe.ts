import {Pipe, PipeTransform} from '@angular/core'
import {DatePipe} from "@angular/common";

@Pipe({
  name: 'dateformat'
})
export class DateformatPipe extends DatePipe implements PipeTransform {

  override transform(value: any): any {
    return super.transform(value, 'dd/MM/yyyy');
  }

  transformDateAndHours(value: any): any {
    return super.transform(value, 'dd/MM/yyyy HH:mm:ss');
  }

}
