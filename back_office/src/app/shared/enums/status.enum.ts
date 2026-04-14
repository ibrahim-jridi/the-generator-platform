import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

export enum StatusEnum {
  ACTIF = 'ACTIF',
  NOT_ACTIF = 'NOT_ACTIF'
}

@Injectable({ providedIn: 'root' })
export class StatusEnumsTranslationService {
  private statusListSubject = new BehaviorSubject<{ id: string; label: string }[]>([]);
  statusList$ = this.statusListSubject.asObservable();

  constructor(private translateService: TranslateService) {
    this.updateStatusList();
    // Update the list whenever the language changes
    this.translateService.onLangChange.subscribe(() => this.updateStatusList());
  }

  private updateStatusList() {
    const translatedList = Object.keys(StatusEnum).map((key) => ({
      id: StatusEnum[key as keyof typeof StatusEnum],
      label: this.translateService.instant(`users.${key}`) // Translate dynamically
    }));
    this.statusListSubject.next(translatedList);
  }

}
