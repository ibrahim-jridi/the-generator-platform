import {Injectable} from "@angular/core";

export enum LegalStatusEnum {
  SARL = 'SARL (Société à Responsabilité Limitée)',
  SUARL = 'SUARL (Société Unipersonnelle à Responsabilité Limitée)',
  SA = 'SA (Société Anonyme)',
}
@Injectable({
  providedIn: 'root',
})
export class LegalStatusClass {

  getLegalStatus() {
    return Object.keys(LegalStatusEnum).map((key) => ({
      id: key,
      label:LegalStatusEnum[key as keyof typeof LegalStatusEnum],
    }));
  }
}
