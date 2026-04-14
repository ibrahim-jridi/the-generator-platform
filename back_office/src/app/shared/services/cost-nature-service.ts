import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {CostNatureEnum} from "../enums/cost-nature.enum";

@Injectable({
  providedIn: 'root',
})
export class CostNatureService {
  private costNatureSubjectList = new BehaviorSubject<{ id: string; label: string }[]>([]);
  costNatureList$ = this.costNatureSubjectList.asObservable();

  constructor(private translateService: TranslateService) {
    this.translateCostNatureService();
    this.translateService.onLangChange.subscribe(() => this.translateCostNatureService());
  }

  translateCostNatureService() {
    const translatedList = Object.keys(CostNatureEnum).map((key) => ({
      id: CostNatureEnum[key as keyof typeof CostNatureEnum],
      label: this.translateService.instant(`configuration.cost.${key.toLowerCase()}`),
    }));
    this.costNatureSubjectList.next(translatedList);
  }
}
