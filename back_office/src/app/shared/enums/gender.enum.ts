import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import { Injectable } from '@angular/core';
import {BehaviorSubject} from "rxjs";

export enum GenderEnum {
  MALE = 'MALE',
  FEMALE = 'FEMALE'
}

@Injectable({ providedIn: 'root' })
export class GenderEnumsTranslationService {
  private genderListSubject = new BehaviorSubject<{ id: string; label: string }[]>([]);
  genderList$ = this.genderListSubject.asObservable();

  constructor(private translateService: TranslateService) {
    this.updateGenderList();
    // Update the list whenever the language changes
    this.translateService.onLangChange.subscribe(() => this.updateGenderList());
  }

  private updateGenderList() {
    const translatedList = Object.keys(GenderEnum).map((key) => ({
      id: GenderEnum[key as keyof typeof GenderEnum],
      label: this.translateService.instant(`users.${key}`) // Translate dynamically
    }));
    this.genderListSubject.next(translatedList);
  }

}
