import {CanActivateChild, Router} from "@angular/router";
import {TokenUtilsService} from "../services/token-utils.service";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class ConnectorManagementGuard implements CanActivateChild {
  constructor(private tokenUtilsService: TokenUtilsService, private router: Router) {}

  canActivateChild(): boolean {
    if (this.tokenUtilsService.isAdmin) {
      return true;
    } else {
      this.router.navigate(['/']);
      return false;
    }
  }
}
