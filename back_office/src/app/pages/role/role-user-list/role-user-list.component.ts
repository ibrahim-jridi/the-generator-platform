import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { UiModalComponent } from '../../../theme/shared/components/modal/ui-modal/ui-modal.component';
import { TranslateModule, TranslatePipe } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';
import { UserService } from '../../../shared/services/user.service';
import { Role } from '../../../shared/models/role.model';
import { RoleService } from '../../../shared/services/role.service';
import { CustomTableModule } from '../../../theme/shared/components/custom-table/custom-table.module';
import { NgIf } from '@angular/common';
import { SharedModule } from '../../../theme/shared/shared.module';

@Component({
  selector: 'app-role-user-list',
  standalone: true,
  imports: [TranslateModule, CustomTableModule, NgIf, SharedModule],
  templateUrl: './role-user-list.component.html',
  styleUrl: './role-user-list.component.scss'
})
export class RoleUserListComponent implements OnInit {
  protected form: FormGroup;
  protected users: any[] = [];
  protected selectedUsers: any[] = [];
  protected roleId: string;
  protected roleName: string;
  public roles: Role[];
  protected userName: string;
  public tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  public actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  @ViewChild('deleteRoleModal') protected deleteRoleModal: UiModalComponent;
  protected userId: string;
  public state = window.history.state;
  protected roleHasNoMoreUser: boolean = false;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private toasterService: AppToastNotificationService,
    private userService: UserService,
    private roleService: RoleService,
    private fb: FormBuilder
  ) {}

  public ngOnInit(): void {
    this.roleId = this.state.roleId;
    this.roleName = this.state.roleName;
    this.initTableColumns();
    this.initTableActions();
    this.loadUsers();
    this.loadAllRoles();
    this.initFormGroup();
  }

  private loadUsers(): void {
    this.userService.getAllUsersByRoleId(this.roleId).subscribe({
      next: (users) => {
        this.users = this.formatIndexUsers(users);
        this.formatUsersList();
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('users.errors.FAILED_TO_LOAD_USERS'), this.translatePipe.transform('menu.ERROR'));
      },
      complete: () => {
        this.checkUserList();
      }
    });
  }

  private formatUsersList(): void {
    this.users.forEach((user) => {
      user.status = user.isActive ? this.translatePipe.transform('groups.ACTIVE') : this.translatePipe.transform('groups.INACTIVE');
    });
  }

  protected formatIndexUsers(usersList: any[]): any[] {
    return usersList.map((item, index) => ({
      ...item,
      index: index + 1
    }));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('users.FIRST_NAME', 'firstName', false));
    this.tableColumns.push(new CustomTableColonneModel('users.LAST_NAME', 'lastName', false));
    this.tableColumns.push(new CustomTableColonneModel('resetPassword.USERNAME', 'username', false));
    //this.tableColumns.push(new CustomTableColonneModel('users.CREATED_DATE', 'createdDate', false));
    this.tableColumns.push(new CustomTableColonneModel('users.EMAIL', 'email', false));
    this.tableColumns.push(new CustomTableColonneModel('users.STATUS', 'status', false));
  }

  private initTableActions() {
    this.actionButton.push(new ButtonActionModel('delete', 'tooltip.DELETE', 'btn btn-icon', 'feather icon-trash'));
  }

  protected buttonAction(event: any): void {
    if (event.buttonActionId === 'delete') {
      this.deleteUserModal.show();
      this.userId = event.objectClicked.id;
      this.userName = event.objectClicked.username;
    }
  }

  protected handleOnDeleteUser(): void {
    this.userService.deleteInternalUser(this.userId).subscribe({
      next: (data) => {
        this.toasterService.onSuccess(this.translatePipe.transform('users.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.deleteUserModal.hide();
      },
      error: (error) => {
        this.toasterService.onError(this.translatePipe.transform('users.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private updateTable(): void {
    this.users = [];
    this.loadUsers();
  }

  private loadAllRoles() {
    this.roleService.findAllRoles().subscribe({
      next: (roles) => {
        this.roles = roles.filter((role) => role.id != this.roleId);
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('roles.ERROR_FETCHING_ROLES'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected onAllCheckedChange(isChecked: boolean): void {
    if (isChecked) {
      this.selectedUsers = this.users;
    } else {
      this.selectedUsers = [];
    }
  }

  protected onItemChecked(event: any): void {
    this.selectedUsers.push(event);
  }

  protected onItemUnChecked(event: any): void {
    this.selectedUsers = this.selectedUsers.filter((user) => user.id != event.id);
  }

  private initFormGroup(): void {
    this.form = this.fb.group({
      selectedRoles: [[], Validators.required]
    });
  }

  protected get selectedRoles(): any {
    return this.form?.get('selectedRoles').getRawValue();
  }

  protected reassignUsersToRoles(): void {
    let newRoles = this.roles.filter((role) => this.selectedRoles.includes(role.id));
    this.selectedUsers.forEach((user) => (user.roles = newRoles));
    this.userService.saveAllUsersRoles(this.selectedUsers).subscribe({
      next: () => {
        this.toasterService.onSuccess(
          this.translatePipe.transform('role.USER_REASSIGNED_SUCCESSFULLY'),
          this.translatePipe.transform('menu.SUCCESS')
        );
        this.updateTable();
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('role.FAILED_TO_REASSIGN_USERS'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private checkUserList(): void {
    if (this.users.length == 0) {
      this.roleHasNoMoreUser = true;
      this.deleteRoleModal.show();
    }
  }

  protected safeDeleteRole(): void {
    this.roleService.deleteRoleById(this.roleId).subscribe({
      next: (res) => {
        this.updateTable();
        this.deleteRoleModal.hide();
        this.toasterService.onSuccess(this.translatePipe.transform('role.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.router.navigate(['/pages/role-management']);
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('role.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }
}
