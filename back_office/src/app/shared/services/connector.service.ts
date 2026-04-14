import {Injectable} from "@angular/core";
import {GlobalService} from "./global.service";
import {Observable} from "rxjs";
import {Connector} from "../models/connector.model";
import {PaginationArgs} from "../models/paginationArgs.model";
import {HttpParams} from "@angular/common/http";
import {RequestType} from "../enums/requestType";
import {FilterValuesModel} from "../models/filter-values-model";
import {ConnectorResponse} from "../models/connector-response.model";

@Injectable({
  providedIn: 'root'
})
export class ConnectorService {

  constructor(private globalService: GlobalService) { }

  public CONNECTORS_URI = this.globalService.BASE_CONNECTOR_URL + this.globalService.API_V1_URL + 'connectors';

  public getConnectors(criteria: FilterValuesModel[], pageable: PaginationArgs): Observable<ConnectorResponse> {
    let params = new HttpParams()
      .append('sort', pageable.sort)
      .append('page', pageable.page)
      .append('size', pageable.size);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue);
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };

    return this.globalService.call(RequestType.GET, this.CONNECTORS_URI, options);
  }

  public saveConnector(connector: Connector): Observable<Connector> {
    return this.globalService.call(RequestType.POST, this.CONNECTORS_URI, connector);
  }
}
