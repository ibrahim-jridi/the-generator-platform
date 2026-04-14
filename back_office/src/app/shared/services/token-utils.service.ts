import {Injectable} from "@angular/core";
import decode from "jwt-decode";
import {Authorities} from "../enums/authorities.enum";

@Injectable({
  providedIn: 'root'
})
export class TokenUtilsService {
  private decryptedToken: any;
  private token: string;
  public isAdmin: boolean = false;


  constructor() {
    this.getToken();
  }

  public hasUserRole(userRole: string): boolean {
    return !!this.decryptedToken && this.decryptedToken.resource_access?.bs_client?.roles?.filter(role => role === userRole).length > 0;
  }

  public isFirstConnection(): boolean {
    return !!this.decryptedToken && this.decryptedToken.firstConnection;
  }

  public getUserId(): string {
    return !!this.decryptedToken ? this.decryptedToken.user_id : null;
  }

  public getSub(): string {
    return !!this.decryptedToken ? this.decryptedToken.sub : null;
  }

  public getUserName(token : string): string {
    const decryptedToken : any = decode(token);
    return decryptedToken.username ;
  }

  public getUserGroup(): string {
    return !!this.decryptedToken ? this.decryptedToken.group : null;
  }

  public isEmailVerified(): boolean {
    return !!this.decryptedToken && this.decryptedToken.email_verified;
  }

  public getName(): string {
    return  !!this.decryptedToken ? this.decryptedToken.name : null;
  }
  public getUsername(): string {
    return  !!this.decryptedToken ? this.decryptedToken.preferred_username : null;
  }

  public getFirstConnection(): string {
    return  !!this.decryptedToken ? this.decryptedToken.first_connection : null;
  }

  private getToken(): void {
    this.token = sessionStorage.getItem('token');
    if (this.token) {
      this.decryptedToken = decode(this.token);
      if (!this.decryptedToken.exp) {
        this.decryptedToken = null;
      }
    } else {
      this.decryptedToken = null;
    }
  }

  public isAuthenticated(): boolean {
    this.getToken();
    let valid = false;
    if (this.decryptedToken && this.decryptedToken.exp && this.decryptedToken.exp > Math.floor(Date.now() / 1000)) {
      this.isAdmin = !!this.decryptedToken && this.decryptedToken.resource_access?.bs_client?.roles?.filter(role => role === "BS_ADMIN").length > 0;
      valid = true;
    }
    return valid;
  }

  public isAdministrator(): boolean {

    this.getToken();
    if (!!this.decryptedToken && this.decryptedToken.resource_access?.bs_client?.roles?.includes(Authorities.BS_ADMIN)) {
      this.isAdmin = true;
    }else{
      this.isAdmin = false;
    }
    return this.isAdmin;
  }

  public isUserGroup(userGroup: string): boolean {
    return !!this.decryptedToken && this.decryptedToken.group === userGroup;
  }

  public clearToken(): void {
    this.token = null;
    this.decryptedToken = null;
    sessionStorage.removeItem('token');
  }

  public isUserExternal(): boolean {
    const username = this.getUsername();
    if (username.substring(0, 3) === 'ext' || (username.substring(0, 2) === 'pm' && username.substring(3, 6) === 'ext')) {
     return true;
    }
    else return  false;
  }

  public isPM(): boolean {
    const username = this.getUsername();
    if (username.substring(0, 2) === 'pm') {
      return true;
    } else return false;
  }

  public isPP(): boolean {
    const username = this.getUsername();
    if (username.substring(0, 3) === 'ext') {
      return true;
    } else return false;
  }
  public isInt(): boolean {
    const username = this.getUsername();
    if (username.substring(0, 3) === 'int') {
      return true;
    } else return false;
  }
  public getAuthorities(): string[] {
    this.getToken();
    return this.decryptedToken?.resource_access?.bs_client?.roles || [];
  }

}
