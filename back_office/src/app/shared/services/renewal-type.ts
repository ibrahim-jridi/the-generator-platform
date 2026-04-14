import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {FrequenceTypeEnum} from "../enums/frequence-type.enum";
import {RenewalTypeEnum} from "../enums/renewal-type.enum";

@Injectable({
  providedIn: 'root',
})
export class RenewalType {
  private renewalTypeSubjectList = new BehaviorSubject<{ id: string; label: string }[]>([]);
  renewalTypeList$ = this.renewalTypeSubjectList.asObservable();

  constructor(private translateService: TranslateService) {
    this.translateRenewalTypeList();
    this.translateService.onLangChange.subscribe(() => this.translateRenewalTypeList());
  }

  translateRenewalTypeList() {
    const translatedList = Object.keys(RenewalTypeEnum).map((key) => ({
      id: RenewalTypeEnum[key as keyof typeof RenewalTypeEnum],
      label: this.translateService.instant(`configuration.cost.${key.toLowerCase()}`)
    }));
    this.renewalTypeSubjectList.next(translatedList);
  }
}
