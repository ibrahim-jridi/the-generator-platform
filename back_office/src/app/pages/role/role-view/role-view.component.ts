import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { RoleService } from '../../../shared/services/role.service';
import { Authority } from '../../../shared/models/authority.model';
import { TokenUtilsService } from '../../../shared/services/token-utils.service';
import { Roles } from '../../../shared/enums/role.enum';

@Component({
  selector: 'app-role-view',
  templateUrl: './role-view.component.html',
  styleUrl: './role-view.component.scss'
})
export class RoleViewComponent implements OnInit {
  protected roleForm: any;
  protected role: any;
  public state = window.history.state;
  protected roleId: string = this.state?.roleId;
  protected permissionsCount: number = 0;
  protected authorities: Authority[];
  protected roleName: string;
  protected roleStatus: string = '';
  protected hasUsers: boolean = false;
  public showButton: boolean = false;
  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private fb: FormBuilder,
    private toastrService: AppToastNotificationService,
    private roleService: RoleService,
    private tokenUtilisService: TokenUtilsService
  ) {}

  public ngOnInit(): void {
    this.roleId = this.state.roleId;
    this.initRoleForm();
    this.loadRoleData(); // Load the role data after initializing the form
  }

  private havePermissionOfAdmin(): void {
    let roleLabel = this.roleForm.controls['labelle'].value;
    if (this.tokenUtilisService.isAdmin) {
      this.showButton = true;
    } else {
      if (roleLabel === Roles.ROLE_ADMIN || roleLabel === Roles.DEFAULT_ROLE) {
        this.showButton = false;
      } else {
        this.showButton = true;
      }
    }
  }
  private initRoleForm(): void {
    this.roleForm = this.fb.group({
      labelle: [{ value: '', disabled: true }, [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)]],
      description: [{ value: '', disabled: true }, [Validators.required, Validators.pattern(/^[a-zA-ZÀ-ÿ\s]+$/)]],
      permission: [{ value: '', disabled: true }, [Validators.required]]
    });
  }

  private loadRoleData(): void {
    this.roleService.getRoleById(this.roleId).subscribe((data) => {
      this.authorities = data.authorities;
      this.roleName = data.label;
      this.permissionsCount = data.authorities.length;
      this.roleStatus = data.isActive ? this.translatePipe.transform('groups.ACTIVE') : this.translatePipe.transform('groups.INACTIVE');
      this.hasUsers = data.hasUsers;
      this.roleForm.patchValue({
        labelle: data.label,
        description: data.description,
        permission: this.permissionsCount
      });
      this.havePermissionOfAdmin();
    });
  }

  protected reassignUsers(roleId: string, roleName: string) {
    const navigationExtras: NavigationExtras = {
      state: {
        roleId: roleId,
        roleName: roleName
      }
    };
    this.router.navigate(['/pages/role-management/list-users-role/' + roleId], navigationExtras);
  }

  protected permanentlyDeleteRole() : void {
    this.roleService.permanentlyDeleteRole(this.roleId).subscribe({
      next: () => {
        this.toastrService.onSuccess(this.translatePipe.transform('role.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.router.navigate(['/pages/role-management']);
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('role.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  public get labelle() {
    return this.roleForm?.get('labelle')!;
  }

  public get description() {
    return this.roleForm?.get('description')!;
  }

  protected safeDeleteRole(): void {
    this.roleService.deleteRoleById(this.roleId).subscribe({
      next: (res) => {
        this.toastrService.onSuccess(this.translatePipe.transform('role.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.router.navigate(['/pages/role-management']);
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('role.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected updateRole(): void {
    const navigationExtras: NavigationExtras = {
      state: {
        roleId: this.roleId // Database id
      }
    };
    this.router.navigate(['/pages/role-management/update-role', this.roleForm.controls['labelle'].value], navigationExtras);
  }
}
