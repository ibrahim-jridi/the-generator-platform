import { CanActivateChild, Router } from '@angular/router';
import { TokenUtilsService } from '../services/token-utils.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GedManagementGuard implements CanActivateChild {
  constructor(private tokenUtilsService: TokenUtilsService, private router: Router) {}

  canActivateChild(): boolean {

      return true;

  }

  checkAuthorities(authority: string): boolean {
    return this.tokenUtilsService.hasUserRole(authority);
  }
}
