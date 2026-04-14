import {Injectable} from "@angular/core";
import {BehaviorSubject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {CostTypeEnum} from "../enums/cost-type.enum";
import {categoryServiceEnum} from "../enums/category-service.enum";

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private categorySubjectList = new BehaviorSubject<{ id: string; label: string }[]>([]);
  categoryList$ = this.categorySubjectList.asObservable();

  constructor(private translateService: TranslateService) {
    this.translateCategoryService();
    this.translateService.onLangChange.subscribe(() => this.translateCategoryService());
  }

  translateCategoryService() {
    const translatedList = Object.keys(categoryServiceEnum).map((key) => ({
      id: categoryServiceEnum[key as keyof typeof categoryServiceEnum],
      label: this.translateService.instant(`configuration.cost.${key.toLowerCase()}`)
    }));
    this.categorySubjectList.next(translatedList);
  }
}
