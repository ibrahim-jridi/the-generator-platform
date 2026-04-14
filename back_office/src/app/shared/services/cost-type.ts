import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {FrequenceTypeEnum} from "../enums/frequence-type.enum";
import {CostTypeEnum} from "../enums/cost-type.enum";

@Injectable({
  providedIn: 'root',
})
export class CostType {
  private costTypeSubjectList = new BehaviorSubject<{ id: string; label: string }[]>([]);
  costTypeList$ = this.costTypeSubjectList.asObservable();

  constructor(private translateService: TranslateService) {
    this.translateCostType();
    this.translateService.onLangChange.subscribe(() => this.translateCostType());
  }

  translateCostType() {
    const translatedList = Object.keys(CostTypeEnum).map((key) => ({
      id: CostTypeEnum[key as keyof typeof CostTypeEnum],
      label: this.translateService.instant(`configuration.cost.${key.toLowerCase()}`)
    }));
    this.costTypeSubjectList.next(translatedList);
  }
}
