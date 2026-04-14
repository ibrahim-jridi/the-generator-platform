import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {BsConfigBackEndModel} from "../models/bsconfigbackend.model";
import {GlobalService} from "./global.service";
import {PaginationArgs} from "../models/paginationArgs.model";
import {CostService} from "../models/cost-service";
import {RequestType} from "../enums/requestType";


@Injectable({
  providedIn: 'root'
})
export class BsConfigService {

  constructor(private globalService: GlobalService) {
  }


  private BsConfigBackEndSource = new BehaviorSubject<BsConfigBackEndModel>({});
  currentPrefix = this.BsConfigBackEndSource.asObservable();

  public cost_service_URI = this.globalService.BASE_PAYMENT_URL + this.globalService.API_V1_URL + 'cost-service';


  initConfig() {
    const bs: BsConfigBackEndModel = {
      prefixRole: 'BS_ROLE_',
      prefixGroup: 'BS_GROUP_',
    };

    this.BsConfigBackEndSource.next(bs);
  }
  public getConfig() : BsConfigBackEndModel {
    return this.BsConfigBackEndSource.getValue();
  }

  public setConfig(config : BsConfigBackEndModel) :void {
    this.BsConfigBackEndSource.next(config);
  }
  public getAllCostServices(criteria: any, paginationArgs: PaginationArgs, categoryType: string): Observable<CostService[]> {
    const url = `${this.cost_service_URI}?categoryService=${categoryType}`;
    return this.globalService.call(RequestType.GET, url, { params: { ...paginationArgs, ...criteria } ,  observe: 'response'});
  }
  public createCostService(costService: CostService): Observable<CostService> {
    return this.globalService.call(RequestType.POST, this.cost_service_URI, costService);
  }
  public getCostServiceById(id: string): Observable<CostService> {
    const url = this.cost_service_URI + '/'+ id;
    return this.globalService.call(RequestType.GET, url);
  }

  public updateCostService(costService: CostService, id: string): Observable<CostService> {
    const url = this.cost_service_URI + '/'+ id;
    return this.globalService.call(RequestType.PUT, url, costService);
  }
  public deleteCostServiceById(id: string): Observable<any> {
    const url = this.cost_service_URI + '/'+ id;
    return this.globalService.call(RequestType.DELETE, url);
  }
}
