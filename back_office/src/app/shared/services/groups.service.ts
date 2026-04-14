import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Group } from '../models/group.model';
import { GlobalService } from './global.service';
import { RequestType } from '../enums/requestType';
import { PaginationArgs } from '../models/paginationArgs.model';
import { FilterValuesModel } from '../models/filter-values-model';
import { GroupResponse } from '../models/group-response.model';


@Injectable({
  providedIn: 'root'
})
export class GroupsService {
  private USERS_URI = 'http://localhost:3005/users';

  constructor(private http: HttpClient, private globalService: GlobalService) {}
  private GROUPS_URI = this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'groups';

  getGroups(): Observable<any[]> {
    return this.http.get<Group[]>(this.GROUPS_URI);
  }

  getGroupById(id: any): Observable<Group> {
    return this.http.get<Group>(`${this.GROUPS_URI}/${id}`);
  }

  addGroup(group: Group): Observable<Group> {
    return this.http.post<Group>(this.GROUPS_URI, group);
  }

  updateGroup(id: string, group: Group): Observable<Group> {
    return this.globalService.call(RequestType.PUT, `${this.GROUPS_URI}/${id}`, group);
  }

  deleteGroup(id: string): Observable<void> {
    return this.http.delete<void>(`${this.GROUPS_URI}/${id}`);
  }

  getUserCountByGroupId(groupId: string): Observable<number> {
    return this.http.get<any[]>(`${this.USERS_URI}?idGroup=${groupId}`).pipe(map((users) => users.length));
  }
  getGroupsByUserId(userId: string, paginationArgs: PaginationArgs): Observable<any> {
    return this.globalService.call(RequestType.GET, this.GROUPS_URI + '/getGroupsByUserId/' + userId, { params: paginationArgs });
  }

  getAllGroups(criteria: FilterValuesModel[], paginationArgs: PaginationArgs): Observable<GroupResponse> {
    let params = new HttpParams()
      .set('page', paginationArgs.page)
      .set('size', paginationArgs.size)
      .set('sort', paginationArgs.sort);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue);
      });
    }

    const options = {
      params: params,
      observe: 'response'
    }

    return this.globalService.call(
      RequestType.GET,
      this.GROUPS_URI, options);
  }
  getGroupsList(): Promise<Group[]> {
    return firstValueFrom(this.globalService.call(RequestType.GET, this.GROUPS_URI + '/all'));
  }

  public activateOrDeactivateGroup(id): Observable<{ message: String }> {
    return this.globalService.call(RequestType.PUT, this.GROUPS_URI + '/activate-or-deactivate-group/' + id);
  }

  public permanentlyDeleteGroup(id) : Observable<{ message: String }> {
    return this.globalService.call(RequestType.DELETE, this.GROUPS_URI + '/delete-group-permanently/' + id);
  }

  public safeDeleteGroup(id) : Observable<{ message: String }> {
    return this.globalService.call(RequestType.DELETE, this.GROUPS_URI + '/safe-delete-group/' + id);
  }

  public fetchAllGroups(): Observable<Group[]> {
    return this.globalService.call(RequestType.GET, this.GROUPS_URI + '/all');
  }

}
