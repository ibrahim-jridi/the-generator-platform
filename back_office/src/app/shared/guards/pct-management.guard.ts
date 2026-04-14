import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { TokenUtilsService } from '../services/token-utils.service';
import { Authorities } from '../enums/authorities.enum';

@Injectable({
  providedIn: 'root'
})
export class PctManagementGuard implements CanActivateChild {
  constructor(private tokenUtilsService: TokenUtilsService, private router: Router) {
  }

  canActivateChild(): boolean {
    if (this.checkAuthorities(Authorities.BS_CREATE_PCT)) {
      return true;
    }
    else if(this.checkAuthorities(Authorities.BS_CREATE_AGENCE_PCT)){
      return true;
    }
    else if(this.checkAuthorities(Authorities.BS_CREATE_SUBSIDIARY_PCT)){
      return true;
    }else {
      this.router.navigate(['/']);
      return false;
    }
  }

  checkAuthorities(authority: string): boolean {
    return this.tokenUtilsService.hasUserRole(authority);
  }
}
