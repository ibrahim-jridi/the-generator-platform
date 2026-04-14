import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'symbol'
})
export class SymbolPipe implements PipeTransform {

  transform(value: any, currencyCode: string): string {
    switch (currencyCode) {
      case 'USD':
        return value == '' ? ' $': value.toFixed(2) + ' $';
      case 'TND':
        return value == '' ? ' DT': value.toFixed(3) + ' DT';
      case 'CFA':
        return value == '' ? ' FCFA': value.toFixed(0) + ' FCFA' ;
      case 'EUR':
        return value == '' ? ' €': value.toFixed(2) + ' €';
      case 'GBP':
        return value == '' ? ' £': value.toFixed(2) + ' £';
      case 'JPY':
        return value == '' ? ' ¥': value.toFixed(0) + ' ¥'; // Yen japonais n'a pas de décimales
      case 'CAD':
        return value == '' ? ' $CAD': value.toFixed(2) + ' $CAD';
      case 'AUD':
        return value == '' ? ' $AUD': value.toFixed(2) + ' $AUD';
      case 'CHF':
        return value == '' ? ' $CHF': value.toFixed(2) + ' $CHF';
      case 'NZD':
        return value == '' ? ' $NZD': value.toFixed(2) + ' $NZD';
      case 'CNY':
        return value == '' ? ' ¥': value.toFixed(2) + ' ¥';
      case 'SEK':
        return value == '' ? ' SEK': value.toFixed(2) + ' SEK';
      case 'NOK':
        return value == '' ? ' NOK': value.toFixed(2) + ' NOK';
      case 'RUB':
        return value == '' ? ' руб': value.toFixed(2) + ' руб';
      case 'MXN':
        return value == '' ? ' $MXN': value.toFixed(2) + ' $MXN';
      case 'ZAR':
        return value == '' ? ' R': value.toFixed(2) + ' R';
      case 'INR':
        return value == '' ? ' ₹': value.toFixed(2) + ' ₹';
      case 'EGP':
        return value == '' ? ' £EGP': value.toFixed(2) + ' £EGP';
      case 'DZD':
        return value == '' ? ' DA': value.toFixed(2) + ' DA';
      case 'KRW':
        return value == '' ? ' ₩': value.toFixed(0) + ' ₩'; // Won sud-coréen n'a pas de décimales
      case 'SGD':
        return value == '' ? ' $SGD': value.toFixed(2) + ' $SGD';
      case 'THB':
        return value == '' ? ' ฿': value.toFixed(2) +  ' ฿';
      case 'PHP':
        return value == '' ? ' ₱': value.toFixed(2) + ' ₱';
      case 'HUF':
        return value == '' ? ' Ft': value.toFixed(2) + ' Ft';
      case 'PLN':
        return value == '' ? ' zł': value.toFixed(2) + ' zł';
      default:
        return value == '' ? ' FCFA': value.toFixed(0) + ' FCFA' ; // Par défaut, renvoyez simplement la valeur formatée avec 2 décimales
    }
  }

}
