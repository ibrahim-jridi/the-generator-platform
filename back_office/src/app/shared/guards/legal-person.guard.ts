import { CanActivateChild, Router } from '@angular/router';
import { TokenUtilsService } from '../services/token-utils.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LegalPersonGuard implements CanActivateChild {
  constructor(private tokenUtilsService: TokenUtilsService, private router: Router) {}

  canActivateChild(): boolean {
    if (this.tokenUtilsService.isAdmin || this.checkAuthorities('BS_LEGAL_PERSON')) {
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
