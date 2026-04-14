import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivateChild,
  Router,
  RouterStateSnapshot
} from '@angular/router';
import { LoginService } from '../services/login.service';
import { TokenUtilsService } from '../services/token-utils.service';
import { Roles } from '../enums/role.enum';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardAdmin implements CanActivateChild {
  constructor(
    private router: Router,
    private loginService: LoginService,
    private tokenUtilsService: TokenUtilsService
  ) {
  }

  canActivateChild(
    childRoute: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.tokenUtilsService.isAuthenticated()) {
      // return this.checkAuthorities();
      return true;
    }
    this.loginService.logout();
    return false;
  }

  private checkAuthorities(): boolean {
    return  this.tokenUtilsService.hasUserRole(Roles.DPM_ADMINISTRATOR);
  }
}
