import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { GlobalService } from './global.service';
import { RequestType } from '../enums/requestType';
import { Authorities } from '../enums/authorities.enum';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  constructor(private globalService: GlobalService) {}

  private dashboardState = new Subject<boolean>();
  getAppVersion(): Observable<any> {
    return this.globalService.call(RequestType.GET, this.globalService.url.appVersion);
  }

  dashboardStateObservable = this.dashboardState.asObservable();

  setDashboardState(isDashboard: boolean) {
    this.dashboardState.next(isDashboard);
  }

  public getCardsData(): any {
    return [
      {
        id: 'users',
        title: 'dashboard.USERS',
        icon: 'icon-User',
        total: 0,
        stats: [
          { value: 34, label: 'dashboard.ACTIVE' },
          { value: 1120, label: 'dashboard.INACTIVE' }
        ]
      },
      {
        id: 'process',
        title: 'dashboard.PROCESS',
        icon: 'icon-process',
        total: 0,
        stats: [
          { value: 20, label: 'dashboard.ACTIVE' },
          { value: 2, label: 'dashboard.SUSPENDED' },
          { value: 12, label: 'dashboard.CLOSED' }
        ]
      },
      {
        id: 'instance',
        title: 'dashboard.INSTANCE',
        icon: 'icon-Forms',
        total: 800,
        stats: [
          { value: 430, label: 'dashboard.ACTIVE' },
          { value: 100, label: 'dashboard.SUSPENDED' },
          { value: 380, label: 'dashboard.CLOSED' }
        ]
      },
      {
        id: 'task',
        title: 'dashboard.TASK',
        icon: 'icon-Forms',
        total: 1000,
        stats: [
          { value: 530, label: 'dashboard.IN_PROGRESS' },
          { value: 462, label: 'dashboard.COMPLETED' }
        ]
      },
      {
        id: 'creating-entities',
        title: 'dashboard.ENTITY_CREATION',
        icon: 'icon-Dashboard-4',
        total: 0,
        stats: [
          { value: 0, label: 'dashboard.COMPANY_CREATION' },
          { value: 0, label: 'dashboard.AUTHORIZATION_GRANTING' },
          { value: 0, label: 'dashboard.MARKET_SURVEILLANCE' }
        ],
        subServices: [
          { key: 'CRO_Model_LTS', title: 'dashboard.CRO_CREATION', authority: Authorities.BS_CREATE_CRO_ACCOUNT },
          { key: 'AgencePromo_Model_LTS', title: 'dashboard.PROMOTION_AGENCY_CREATION', authority: Authorities.BS_CREATE_PROMOT_AGENCY },
          { key: 'IMPORT_EXPORT', title: 'dashboard.IMPORT_EXPORT_CREATION', authority: Authorities.BS_CREATE_IMPORT_EXPORT },
          { key: '', title: 'dashboard.IMMEDIATE_PHARMACY_CREATION', authority: Authorities.BS_CREATE_PHARMACY },
          { key: '', title: 'dashboard.PHARMACY_OPENING', authority: Authorities.BS_OPEN_PHARMACY },
          { key: '', title: 'dashboard.PARAPHARMACY_CREATION', authority: Authorities.BS_CREATE_PARAPHARAMACY },
          { key: '', title: 'dashboard.WHOLESALER_CREATION', authority: Authorities.BS_CREATE_WHOLESALER }
        ]
      },
      {
        id: 'licenses',
        title: 'dashboard.LICENSES_AUTHORIZATIONS',
        icon: 'icon-Dashboard-3',
        total: 0,
        stats: [
          { value: 0, label: 'dashboard.ACTIVE' },
          { value: 0, label: 'dashboard.SUSPENDED' },
          { value: 0, label: 'dashboard.CLOSED' }
        ],
        subServices: [
          { key: '', title: 'dashboard.PHARMA_LICENSE' },
          { key: '', title: 'dashboard.VET_LICENSE' }
        ]
      },
      {
        id: 'list-management',
        title: 'dashboard.WAITLIST_MANAGEMENT',
        icon: 'icon-Dashboard-5',
        total: 0,
        stats: [
          { value: 0, label: 'dashboard.ACTIVE' },
          { value: 0, label: 'dashboard.SUSPENDED' },
          { value: 0, label: 'dashboard.CLOSED' }
        ],
        subServices: [
          { key: 'INSCRIPTION_LISTE_ATTENTE', title: 'dashboard.WAITLIST_REGISTRATION' },
          { key: '', title: 'dashboard.WAITLIST_CHANGE' },
          { key: 'RENOUVELLEMENT_CATEGORIE_LISTE_ATTENTE', title: 'dashboard.WAITLIST_RENEWAL' },
          { key: 'DESINSCRIPTION_LISTE_ATTENTE', title: 'dashboard.WAITLIST_UNREGISTRATION' }
        ]
      },
      {
        id: 'pharmacy-management',
        title: 'dashboard.PHARMACY_MANAGEMENT',
        icon: 'icon-Dashboard-2',
        total: 0,
        stats: [
          { value: 0, label: 'dashboard.ACTIVE' },
          { value: 0, label: 'dashboard.SUSPENDED' },
          { value: 0, label: 'dashboard.CLOSED' }
        ],
        subServices: [
          { key: 'REAMENAGEMENT_OFFICINE', title: 'dashboard.PHARMACY_REORGANIZATION' },
          { key: 'TRANSFER_OFFICINE', title: 'dashboard.PHARMACY_TRANSFER' },
          { key: 'PHARMACY_IN_CASE_OF_DEATH', title: 'dashboard.PHARMACY_DEATH_MANAGEMENT' },
          { key: '', title: 'dashboard.PHARMACIST_REPLACEMENT' },
          { key: '', title: 'dashboard.PHARMACY_SELL_BUY' }
        ]
      },
      {
        id: 'wholesaler-management',
        title: 'dashboard.WHOLESALER_MANAGEMENT',
        icon: 'icon-Forms',
        total: 0,
        stats: [
          { value: 0, label: 'dashboard.ACTIVE' },
          { value: 0, label: 'dashboard.SUSPENDED' },
          { value: 0, label: 'dashboard.CLOSED' }
        ],
        subServices: [
          { key: '', title: 'dashboard.WHOLESALER_RESPONSIBLE_CHANGE' },
          { key: 'EXTENSION', title: 'dashboard.WHOLESALER_PREMISES_EXTENSION' },
          { key: 'ADD_SUBSIDIARY', title: 'dashboard.WHOLESALER_SUBSIDIARY_ADD' }
        ]
      }
    ];
  }
}
