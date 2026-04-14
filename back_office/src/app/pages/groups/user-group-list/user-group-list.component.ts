import {Component, OnInit, ViewChild} from '@angular/core';
import {
  CustomTableColonneModel
} from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import {
  ButtonActionModel
} from '../../../theme/shared/components/custom-table/model/button-action.model';
import {TranslateModule, TranslatePipe} from '@ngx-translate/core';
import {AppToastNotificationService} from '../../../shared/services/appToastNotification.service';
import {UserService} from '../../../shared/services/user.service';
import {CustomTableModule} from '../../../theme/shared/components/custom-table/custom-table.module';
import {NgIf} from '@angular/common';
import {SharedModule} from '../../../theme/shared/shared.module';
import {GroupsService} from '../../../shared/services/groups.service';
import {Group} from '../../../shared/models/group.model';
import {UiModalComponent} from '../../../theme/shared/components/modal/ui-modal/ui-modal.component';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-group-list',
  standalone: true,
  imports: [TranslateModule, CustomTableModule, NgIf, SharedModule],
  templateUrl: './user-group-list.component.html',
  styleUrl: './user-group-list.component.scss'
})
export class UserGroupListComponent implements OnInit {
  protected form: FormGroup;
  protected users: any[] = [];
  protected selectedUsers: any[] = [];
  protected groupId: string;
  protected groupName: string;
  public groups: Group[];
  protected userName: string;
  public tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  public actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  @ViewChild('deleteGroupModal') protected deleteGroupModal: UiModalComponent;
  protected userId: string;
  public state = window.history.state;
  protected groupHasNoMoreUser: boolean = false;

  constructor(
    private translatePipe: TranslatePipe,
    private router: Router,
    private toasterService: AppToastNotificationService,
    private userService: UserService,
    private groupService: GroupsService,
    private fb: FormBuilder
  ) {}

  public ngOnInit(): void {
    this.groupId = this.state.groupId;
    this.groupName = this.state.groupName;
    this.initTableColumns();
    this.initTableActions();
    this.loadUsers();
    this.loadAllGroups();
    this.initFormGroup();
  }

  private loadUsers(): void {
    this.userService.getAllUsersByGroup(this.groupId).subscribe({
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
        this.updateTable();
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

  private loadAllGroups() {
    this.groupService.fetchAllGroups().subscribe({
      next: (groups) => {
        this.groups = groups.filter((group) => group.id != this.groupId);
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('groups.ERROR_FETCHING_GROUPS'), this.translatePipe.transform('menu.ERROR'));
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
      selectedGroups: [[], Validators.required]
    });
  }

  protected get selectedGroups(): any {
    return this.form?.get('selectedGroups').getRawValue();
  }

  protected reassignUsersToGroups(): void {
    let newGroups = this.groups.filter((group) => this.selectedGroups.includes(group.id));
    this.selectedUsers.forEach((user) => (user.groups = newGroups));
    this.userService.saveAllUsersGroups(this.selectedUsers).subscribe({
      next: () => {
        this.toasterService.onSuccess(
          this.translatePipe.transform('groups.USER_REASSIGNED_SUCCESSFULLY'),
          this.translatePipe.transform('menu.SUCCESS')
        );
        this.updateTable();
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('groups.FAILED_TO_REASSIGN_USERS'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private checkUserList(): void {
    if (this.users.length == 0) {
      this.groupHasNoMoreUser = true;
      this.deleteGroupModal.show();
    }
  }

  protected safeDeleteGroup(): void {
    this.groupService.deleteGroup(this.groupId).subscribe({
      next: () => {
        this.toasterService.onSuccess(this.translatePipe.transform('groups.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.router.navigate(["/pages/group-management"]);
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('groups.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }
}
