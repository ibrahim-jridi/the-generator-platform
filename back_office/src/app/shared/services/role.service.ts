import { Injectable } from '@angular/core';
import { GlobalService } from './global.service';
import { BehaviorSubject, firstValueFrom, Observable, of } from 'rxjs';
import { RequestType } from '../enums/requestType';
import { Role } from '../models/role.model';
import { HttpParams } from '@angular/common/http';
import { Authorities } from '../enums/authorities.enum';
import { PaginationArgs } from '../models/paginationArgs.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  public authoritiesSubject = new BehaviorSubject<string[]>([]);

  constructor(private globalService: GlobalService) {}

  private roleUri = this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'roles';

  public findAllRoles(): Observable<Role[]> {
    return this.globalService.call(RequestType.GET, this.roleUri + '/all');
  }

  public deleteRoleById(id: string): Observable<void> {
    return this.globalService.call(RequestType.DELETE, this.roleUri + '/' + id);
  }

  public updateRole(id: string, role: Role): Observable<void> {
    return this.globalService.call(RequestType.PUT, this.roleUri + '/' + id, role);
  }

  public getRoleById(id: string): Observable<Role> {
    return this.globalService.call(RequestType.GET, this.roleUri + '/' + id);
  }

  public findRolesByGroupId(id: string): Observable<Role[]> {
    let params = new HttpParams().set('groupId', id);
    return this.globalService.call(RequestType.GET, this.roleUri + '/by-group', { params: params });
  }

  public getRoleAuthorities(roleLabel: string): Observable<string[]> {
    return of([Authorities.DPM_ADMINISTRATOR]);
  }

  public getAllRoles(criteria: any, paginationArgs: PaginationArgs): Observable<Role[]> {
    return this.globalService.call(RequestType.GET, this.roleUri, criteria, { params: paginationArgs });
  }

  public createRole(roleDTO: Role): Observable<Role> {
    return this.globalService.call(RequestType.POST, this.roleUri, roleDTO);
  }

  public getRoles(): Promise<Role[]> {
    return firstValueFrom(this.globalService.call(RequestType.GET, this.roleUri + '/all'));
  }

  public activateOrDeactivateRoleById(id: string): Observable<void> {
    return this.globalService.call(RequestType.GET, this.roleUri + '/activate-or-deactivate-role-by-id/' + id);
  }

  public permanentlyDeleteRole(id): Observable<{ message: string }> {
    return this.globalService.call(RequestType.DELETE, this.roleUri + '/delete-role-permanently/' + id);
  }

  public findInterneRoles(): Observable<Role[]> {
    return this.globalService.call(RequestType.GET, this.roleUri + '/get-interne-roles');
  }
}
