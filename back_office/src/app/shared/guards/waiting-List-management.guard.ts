import { CanActivateChild, Router } from '@angular/router';
import { TokenUtilsService } from '../services/token-utils.service';
import { Injectable } from '@angular/core';
import { Authorities } from '../enums/authorities.enum';

@Injectable({
  providedIn: 'root'
})
export class WaitingListManagementGuard implements CanActivateChild {
  constructor(private tokenUtilsService: TokenUtilsService, private router: Router) {}

  canActivateChild(): boolean {
    if (this.checkAuthorities(Authorities.BS_WAITLIST_REGISTRATION)) {
      return true;
    }
    else if(this.checkAuthorities(Authorities.BS_WAITLIST_UNSUBSCRIBE)){
      return true;
    } else {
      this.router.navigate(['/']);
      return false;
    }
  }

  checkAuthorities(authority: string): boolean {
    return this.tokenUtilsService.hasUserRole(authority);
  }
}
