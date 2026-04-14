import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'dateTimeFormat'
})
export class DateTimeFormatPipe extends DatePipe implements PipeTransform {
  override transform(value: any): any {
    return super.transform(value, 'dd/MM/yyyy HH:mm:ss', 'en-US');
  }
}
