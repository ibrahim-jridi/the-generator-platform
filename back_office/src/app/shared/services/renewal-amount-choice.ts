import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {RenewalTypeEnum} from "../enums/renewal-type.enum";
import {RenewalAmountChoiceEnum} from "../enums/renewal-amount-choice.enum";

@Injectable({
  providedIn: 'root',
})
export class RenewalAmountChoice {
  private renewalAmountChoiceSubjectList = new BehaviorSubject<{ id: string; label: string }[]>([]);
  renewalAmountChoiceList$ = this.renewalAmountChoiceSubjectList.asObservable();

  constructor(private translateService: TranslateService) {
    this.translateRenewalAmountChoiceList();
    this.translateService.onLangChange.subscribe(() => this.translateRenewalAmountChoiceList());
  }

  translateRenewalAmountChoiceList() {
    const translatedList = Object.keys(RenewalAmountChoiceEnum).map((key) => ({
      id: RenewalAmountChoiceEnum[key as keyof typeof RenewalAmountChoiceEnum],
      label: this.translateService.instant(`configuration.cost.${key.toLowerCase()}`)
    }));
    this.renewalAmountChoiceSubjectList.next(translatedList);
  }
}
