import { CanActivateChild, Router } from '@angular/router';
import { TokenUtilsService } from '../services/token-utils.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TasksByGroupManagementGuard implements CanActivateChild {
  constructor(private tokenUtilsService: TokenUtilsService, private router: Router) {}

  canActivateChild(): boolean {
    if (this.tokenUtilsService.isAdmin || this.checkAuthorities('BS_TASKS_BY_GROUP')) {
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
