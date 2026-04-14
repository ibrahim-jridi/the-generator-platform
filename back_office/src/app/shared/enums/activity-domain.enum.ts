import {Injectable} from "@angular/core";

export enum ActivityDomainEnum {
  INDUSTRIE = 'Industrie',
  SERVICES = 'Services',
  COMMERCE = 'Commerce',
  AGRICULTURE_PECHE = 'Agriculture et pêche',
  TOURISME_LOISIR = 'Tourisme et loisirs',
  ENERGIE_ENVIRONNEMENT = 'Énergie et environnement',
  BATIMENT_TRAVAUX_PUBLIC = 'Bâtiment et travaux publics (BTP)',
  EDUCATION_SANTE = 'Éducation et santé',
  ARTISANAT_CULTURE = 'Artisanat et culture',
  STARTUP_INNOVATION = 'Startups et innovation'
}
@Injectable({
  providedIn: 'root',
})
export class ActivityDomainClass {

  getActivity() {
    return Object.keys(ActivityDomainEnum).map((key) => ({
      id: key,
      label:ActivityDomainEnum[key as keyof typeof ActivityDomainEnum],
    }));
  }
}
