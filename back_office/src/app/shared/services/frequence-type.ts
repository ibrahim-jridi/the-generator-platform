import {Injectable} from "@angular/core";
import {FrequenceTypeEnum} from "../enums/frequence-type.enum";
import {BehaviorSubject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root',
})
export class FrequenceType {
  private frequenceTypeSubjectList = new BehaviorSubject<{ id: string; label: string }[]>([]);
  frequenceTypeList$ = this.frequenceTypeSubjectList.asObservable();

  constructor(private translateService: TranslateService) {
    this.translateFrequenceType();
    this.translateService.onLangChange.subscribe(() => this.translateFrequenceType());
  }

  translateFrequenceType() {
    const translatedList = Object.keys(FrequenceTypeEnum).map((key) => ({
      id: FrequenceTypeEnum[key as keyof typeof FrequenceTypeEnum],
      label: this.translateService.instant(`configuration.cost.${key.toLowerCase()}`)
    }));
    this.frequenceTypeSubjectList.next(translatedList);
  }
}
