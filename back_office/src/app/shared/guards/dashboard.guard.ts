import { CanActivateChild, Router } from '@angular/router';
import { TokenUtilsService } from '../services/token-utils.service';
import { Injectable } from '@angular/core';
import { LoginService } from '../services/login.service';

@Injectable({
  providedIn: 'root'
})
export class DashboardGuard implements CanActivateChild {
  constructor(private tokenUtilsService: TokenUtilsService ,private loginService: LoginService,) {}

  canActivateChild(): boolean {
    // if (this.tokenUtilsService.isAdmin || this.checkAuthorities('BS_USER')) {
      return true;
    // } else {
    //   this.loginService.logout();
    //   return false;
    // }
  }

  checkAuthorities(authority: string): boolean {
    return this.tokenUtilsService.hasUserRole(authority);
  }
}
