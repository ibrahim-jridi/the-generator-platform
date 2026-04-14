import { Component, OnInit } from '@angular/core';
import { GroupsService } from 'src/app/shared/services/groups.service';
import { NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { UserService } from '../../../shared/services/user.service';
import {
  CustomTableColonneModel
} from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';

@Component({
  selector: 'app-view-group',
  templateUrl: './view-group.component.html',
  styleUrl: './view-group.component.scss'
})
export class ViewGroupComponent implements OnInit {
  protected group: any;
  protected users: any[] = [];
  protected groupId: string;
  protected parentGroupName: string;
  protected hasParent: boolean = false;
  public tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();

  constructor(
    private readonly groupsService: GroupsService,
    private readonly userService: UserService,
    private readonly router: Router,
    private readonly translatePipe: TranslatePipe,
    private readonly toasterService: AppToastNotificationService
  ) {}

  public ngOnInit(): void {
    this.groupId = window.history.state.groupId;
    this.handleOnGetGroup(this.groupId);
  }

  public handleOnGetGroup(id: string): void {
    this.groupsService.getGroupById(id).subscribe({
      next: (data: any) => {
        data.status = data.isActive ? this.translatePipe.transform('groups.ACTIVE') : this.translatePipe.transform('groups.INACTIVE');
        this.group = data;
        this.checkParent();
        this.getGroupUsers();
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('groups.FAILED_TO_LOAD_GROUP'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private getGroupUsers(): void {
    if (this.group.numberOfUsers > 0) {
      this.initTableColumns();
      this.userService.getAllUsersByGroup(this.groupId).subscribe({
        next: (data) => {
          this.users = data;
          this.users.forEach((user) => {
            user.status = user.isActive ? this.translatePipe.transform('groups.ACTIVE') : this.translatePipe.transform('groups.INACTIVE');
          });
        },
        error: () => {
          this.toasterService.onError(this.translatePipe.transform('groups.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
        }
      });
    }
  }

  private checkParent() {
    this.hasParent = this.group.parent;
    if (this.hasParent) {
      this.groupsService.getGroupById(this.group.parent.id).subscribe({
        next: (data: any) => {
          this.parentGroupName = data.label;
        },
        error: () => {
          this.toasterService.onError(this.translatePipe.transform('groups.FAILED_TO_LOAD_PARENT_GROUP'), this.translatePipe.transform('menu.ERROR'));
        }
      });
    }
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('users.FIRST_NAME', 'firstName', false));
    this.tableColumns.push(new CustomTableColonneModel('users.LAST_NAME', 'lastName', false));
    this.tableColumns.push(new CustomTableColonneModel('resetPassword.USERNAME', 'username', false));
    //this.tableColumns.push(new CustomTableColonneModel('users.CREATED_DATE', 'createdDate', false));
    this.tableColumns.push(new CustomTableColonneModel('users.EMAIL', 'email', false));
    this.tableColumns.push(new CustomTableColonneModel('users.STATUS', 'status', false));
  }

  protected reassignUsers(groupId : string, label : string): void {
    const navigationExtras: NavigationExtras = {
      state: {
        groupId: groupId,
        groupName: label
      }
    };
    this.router.navigate(['/pages/group-management/list-users-group/' + groupId], navigationExtras);
  }

  protected safeDeleteGroup(): void {
    this.groupsService.deleteGroup(this.groupId).subscribe({
      next: () => {
        this.toasterService.onSuccess(this.translatePipe.transform('groups.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.router.navigate(['/pages/group-management']);
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('groups.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected permanentlyDeleteGroup(): void {
    this.groupsService.permanentlyDeleteGroup(this.groupId).subscribe({
      next: () => {
        this.toasterService.onSuccess(this.translatePipe.transform('groups.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.router.navigate(['/pages/group-management']);
      },
      error: () => {
        this.toasterService.onError(this.translatePipe.transform('groups.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

}
