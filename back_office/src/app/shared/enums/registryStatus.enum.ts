import {Injectable} from "@angular/core";
import {LegalStatusEnum} from "./legal-status.enum";
import {BehaviorSubject} from "rxjs";
import {TranslateService} from "@ngx-translate/core";
import {GenderEnum} from "./gender.enum";

export enum RegistryStatusEnum {
  ACTIVE = 'ACTIVE',

  IN_STOP = 'IN_STOP',

  SUSPENDED = 'SUSPENDED',
}@Injectable({
  providedIn: 'root',
})
export class RegistryStatusClass {
  private registryStatusSubjectList = new BehaviorSubject<{ id: string; label: string }[]>([]);
  registryStatusList$ = this.registryStatusSubjectList.asObservable();

  constructor(private translateService: TranslateService) {
    this.updateRegistryStatus();
    this.translateService.onLangChange.subscribe(() => this.updateRegistryStatus());
  }

  updateRegistryStatus() {
    const translatedList = Object.keys(RegistryStatusEnum).map((key) => ({
      id: RegistryStatusEnum[key as keyof typeof RegistryStatusEnum],
      label: this.translateService.instant(`users.${key}`)
    }));
    this.registryStatusSubjectList.next(translatedList);
  }
}
