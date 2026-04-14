import {Injectable} from '@angular/core';
import {firstValueFrom, Observable, of} from 'rxjs';
import {GlobalService} from "./global.service";
import {RequestType} from "../enums/requestType";
import {User} from '../models/user.model';
import {PaginationArgs} from '../models/paginationArgs.model';
import {HttpParams} from "@angular/common/http";
import {UserResponseModel} from "../models/user-response-model";
import {FilterValuesModel} from "../models/filter-values-model";
import {CompanyUserModel} from "../models/company-user-model";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private globalService: GlobalService) {
  }

  public USERS_URI = this.globalService.BASE_USER_MANAGEMENT_URL + this.globalService.API_V1_URL + 'user';


  public getUsers(
    criteria: FilterValuesModel[],
    paginationArgs: PaginationArgs
  ): Observable<UserResponseModel> {
    let params = new HttpParams()
      .append('sort', paginationArgs.sort)
      .append('page', paginationArgs.page)
      .append('size', paginationArgs.size);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue.toString());
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };

    return this.globalService.call(
      RequestType.GET,
      this.USERS_URI,
      options
    );
  }

  public getUsersByGroup(criteria: FilterValuesModel[], groupId: any, pageable: PaginationArgs): Observable<UserResponseModel> {
    let params = new HttpParams()
      .append('sort', pageable.sort)
      .append('page', pageable.page)
      .append('size', pageable.size)
      .append('groupId.equals', groupId);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue);
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };

    return this.globalService.call(
      RequestType.GET,
      this.USERS_URI, options);
  }

  public deleteUserById(id: string): Observable<void> {
    return this.globalService.call(
      RequestType.DELETE, this.USERS_URI + '/' + id);
  }

  public saveUser(user: User): Observable<User> {
    return this.globalService.call(
      RequestType.POST, this.USERS_URI, user);
  }

  getAllUsers(): Promise<User[]> {
    return firstValueFrom(this.globalService.call(RequestType.GET, this.USERS_URI + '/all'));
  }
  getUserByIdPromise(id: string):  Promise<User> {
    return firstValueFrom( this.globalService.call(
      RequestType.GET,`${this.USERS_URI}/${id}`));
  }
  getUserById(id: string): Observable<User> {
    return this.globalService.call(
      RequestType.GET,`${this.USERS_URI}/${id}`);
  }

  addUser(user: any, confirm: boolean): Observable<any> {
    const url = `${this.USERS_URI}?confirm=${confirm}`;
    return this.globalService.call(RequestType.POST, url, user);
  }
  addCompanyUser(companyUser: any): Observable<any> {
    return this.globalService.call(RequestType.POST, `${this.USERS_URI}/company-user`,companyUser)
  }
  updateUser(id: string, user: User): Observable<User> {
    return this.globalService.call(RequestType.PUT,`${this.USERS_URI}/${id}`, user);
  }
  updateCompanyUser(id: string, user: CompanyUserModel): Observable<CompanyUserModel> {
    return this.globalService.call(RequestType.PUT,`${this.USERS_URI}/${id}`, user);
  }


  deleteUser(id: string): Observable<void> {
    return this.globalService.call(RequestType.DELETE,`${this.USERS_URI}/${id}`);
  }

  deleteInternalUser(id: string): Observable<User> {
    return this.globalService.call(RequestType.DELETE,this.USERS_URI+'/'+id );
  }
  getInternalUsersByGroupId(
    criteria: FilterValuesModel[],
    groupId: string,
    paginationArgs: PaginationArgs
  ): Observable<UserResponseModel> {

    let params = new HttpParams()
      .append('sort', paginationArgs.sort)
      .append('page', paginationArgs.page)
      .append('size', paginationArgs.size);

    if (criteria != undefined && criteria.length > 0) {
      criteria.forEach((element) => {
        params = params.append(`${element.columnName}.contains`, element.filterValue.toString());
      });
    }

    const options = {
      params: params,
      observe: 'response'
    };

    return this.globalService.call(RequestType.GET, this.USERS_URI + '/getUsersByGroupId/' + groupId, options)
  }
  getUserExternals(): Observable<any[]> {
    return of([
      {
        "n": 1,
        "email": "n.mejai@yopmail.com",
        "firstName": "nour",
        "lastName": "mejai",
        "raison_sosial": "",
        "cin": "07452398",
        "status":"active",
        "id": 2067,
      },
      {
        "n": 2,
        "email": "y.marouani@yopmail.com",
        "firstName": "yasmine",
        "lastName": "Marouani",
        "raison_sosial": "",
        "cin": "07456789",
        "status":"active",
        "id": 2068,
      },
      {
        "n": 3,
        "email": "s.boudawara@yopmail.com",
        "firstName": "Samir",
        "lastName": "boudawara",
        "raison_sosial": "",
        "cin": "07145986",
        "status":"notactive",
        "id": 2069,
      },
      {
        "n": 4,
        "email": "s.benchikh@yopmail.com",
        "firstName": "samia",
        "lastName": "benchikh",
        "raison_sosial": "",
        "cin": "07236597",
        "status":"notactive",
        "id": 2070,
      },
      {
        "n": 5,
        "email": "k.labiadh@yopmail.com",
        "firstName": "khalil",
        "lastName": "Labiadh",
        "raison_sosial": "",
        "cin": "07849634",
        "status":"notactive",
        "id": 2071,
      },
      {
        "n": 6,
        "email": "s.dargachi@yopmail.com",
        "firstName": "slim",
        "lastName": "Dargachi",
        "raison_sosial": "",
        "cin": "0723579",
        "status":"active",
        "id": 2072,
      },
      {
        "n": 7,
        "email": "f.Kouched@yopmail.com",
        "firstName": "Fathi",
        "lastName": "Kouched",
        "raison_sosial": "",
        "cin": "07125874",
        "status":"active",
        "id": 2073,
      },
      {
        "n": 8,
        "email": "i.hajjar@yopmail.com",
        "firstName": "Imed",
        "lastName": "hajjar",
        "raison_sosial": "",
        "cin": "07123968",
        "status":"notactive",
        "id": 2074,
      },
      {
        "n": 9,
        "email": "i.hajjar@yopmail.com",
        "firstName": "Nooman",
        "lastName": "fehri",
        "raison_sosial": "",
        "cin": "07369258",
        "status":"active",
        "id": 2075,
      },
      {
        "n": 10,
        "email": "n.sehli@yopmail.com",
        "firstName": "nadia",
        "lastName": "sehli",
        "raison_sosial": "",
        "cin": "07425987",
        "status":"notactive",
        "id": 2076,
      }
    ]);
  }

  public getAllUsersByGroup(groupId: string): Observable<User[]> {
    return this.globalService.call(RequestType.GET, this.USERS_URI + '/get-all-group-users-by-group-id/' + groupId);
  }

  public saveAllUsersGroups(selectedUsers: any[]) : Observable<any> {
    return this.globalService.call(RequestType.POST, this.USERS_URI + '/reassign-users-to-new-groups', selectedUsers);
  }

  public saveAllUsersRoles(selectedUsers: any[]) : Observable<any> {
    return this.globalService.call(RequestType.POST, this.USERS_URI + '/reassign-users-to-new-roles', selectedUsers);
  }

  public getAllUsersByRoleId(roleId: string): Observable<User[]> {
    return this.globalService.call(RequestType.GET, this.USERS_URI + '/get-all-role-users-by-role-id/' + roleId);
  }

  public getAllPhysicalUsers(): Observable<User[]> {
    return this.globalService.call(RequestType.GET, this.USERS_URI + '/get-all-physical-users');
  }

}
