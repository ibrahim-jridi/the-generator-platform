import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class EnumTranslationService {
  private translationSubjects: { [key: string]: BehaviorSubject<{ id: string; label: string }[]> } = {};

  constructor(private translateService: TranslateService) {}

  initTranslationList(enumType: any, translationKeyPrefix: string): BehaviorSubject<{ id: string; label: string }[]> {
    if (!this.translationSubjects[translationKeyPrefix]) {
      this.translationSubjects[translationKeyPrefix] = new BehaviorSubject<{ id: string; label: string }[]>([]);
      this.updateTranslationList(enumType, translationKeyPrefix);
      this.translateService.onLangChange.subscribe(() => this.updateTranslationList(enumType, translationKeyPrefix));
    }
    return this.translationSubjects[translationKeyPrefix];
  }

  private updateTranslationList(enumType: any, translationKeyPrefix: string) {
    const translatedList = Object.keys(enumType).map((key) => ({
      id: enumType[key as keyof typeof enumType],
      label: this.translateService.instant(`${translationKeyPrefix}.${key}`)
    }));
    this.translationSubjects[translationKeyPrefix].next(translatedList);
  }
}
