import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import {TranslatePipe, TranslateService} from '@ngx-translate/core';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { ButtonActionModel } from '../../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../../shared/models/paginationArgs.model';
import { UserService } from '../../../../shared/services/user.service';
import { UserResponseModel } from '../../../../shared/models/user-response-model';
import { User } from '../../../../shared/models/user.model';
import { FilterValuesModel } from '../../../../shared/models/filter-values-model';

@Component({
  selector: 'app-internal-user-list',
  templateUrl: './internal-user-list.component.html',
  styleUrl: './internal-user-list.component.scss'
})
export class InternalUserListComponent implements OnInit {
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('lastModifiedDate', PaginationSortOrderType.DESC);
  protected users: User[] = [];
  public groupId: string;
  public totalCount: number;
  protected pageSize: number;
  public pageIndex: number = 0;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;
  protected userId: string;
  protected userName: string;
  protected totalElements: number;
  private paginationArgs: PaginationArgs;
  protected viewUsersOfGroup: boolean = false;
  public state = window.history.state;
  filterValues: FilterValuesModel[];
  isExternalUser: boolean;
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private toastrService: AppToastNotificationService,
    private userService: UserService,
    private cdRef: ChangeDetectorRef,
    private translateService: TranslateService
  ) {}

  public ngOnInit(): void {
    this.groupId = this.state.groupId;
    if (this.state.viewUsersOfGroup != null) {
      this.viewUsersOfGroup = this.state.viewUsersOfGroup;
    }
    this.pageSize = 5;
    this.initTableAction();
    this.initTableColumns();
    this.loadUsers();
    this.translateService.onLangChange.subscribe({
      next: () =>{
      this.loadUsers();
    }});
  }

  protected loadUsers(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.pageIndex, size: this.pageSize };
    const statusActive = this.translatePipe.transform('users.ACTIF');
    const statusNotActive = this.translatePipe.transform('users.NOT_ACTIF');
     if (!this.filterValues) {
        this.filterValues = [];
      }
    const repNameFilterIndex = this.filterValues.findIndex(f => f.columnName === 'representativeName');

    if (repNameFilterIndex !== -1) {
      const searchValue = this.filterValues[repNameFilterIndex].filterValue.trim();
      const nameParts = searchValue.split(' ').filter(p => p.trim() !== '');

      if (nameParts.length === 1) {

        this.filterValues.push({ columnName: 'lastName', filterValue: nameParts[0] });

      } else if (nameParts.length > 1) {
        this.filterValues.push({ columnName: 'firstName', filterValue: nameParts[0] });
        this.filterValues.push({ columnName: 'lastName', filterValue: nameParts.slice(1).join(' ') });
      }
    }

     this.filterValues = this.filterValues.filter(f => f.filterValue && f.filterValue.trim() !== '');

    if (this.viewUsersOfGroup == true && this.groupId != null) {
      this.userService.getInternalUsersByGroupId(this.filterValues, this.groupId, this.paginationArgs).subscribe({
        next: (response: UserResponseModel) => {
          const totalCountHeader = response?.headers.get('X-Total-Count');
          this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;

          this.users = response?.body?.map((item, index) => ({
            ...item,
            index: this.pageSize * this.pageIndex + index + 1,
            representativeName: item.firstName ? item.firstName : item?.taxRegistration,
            statut: item.isActive ? statusActive : statusNotActive
          }));
        },
        error: () => {
          this.toastrService.onError(this.translatePipe.transform('users.errors.FAILED_TO_LOAD_USERS'), this.translatePipe.transform('menu.ERROR'));
        }
      });
    } else {
      this.userService.getUsers(this.filterValues, this.paginationArgs).subscribe({
        next: (response: UserResponseModel) => {
          const totalCountHeader = response?.headers.get('X-Total-Count');
          this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
          this.users = response?.body?.map((item, index) => ({
            ...item,
            index: this.pageIndex * this.pageSize + index + 1,
            representativeName: item.firstName ? ( item.firstName + '  ' + item.lastName) : item?.taxRegistration,
            statut: item.isActive ? statusActive : statusNotActive
          }));
        },
        error: () => {
          this.toastrService.onError(this.translatePipe.transform('users.errors.FAILED_TO_LOAD_USERS'), this.translatePipe.transform('menu.ERROR'));
        }
      });
    }
  }

  public nextPage(event): void {
    this.filterValues = event.filterValues;
    this.pageIndex = event.pageNumber - 1;
    this.loadUsers();
  }

  public onPageSizeChange(newPageSize: number) {
    this.pageIndex = 0;
    this.pageSize = newPageSize;
    this.loadUsers();
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      if (event.objectClicked.userType === 'COMPANY') {
        this.handleOnViewUser(event.objectClicked.id, event.objectClicked.socialReason);
      } else {
        this.handleOnViewUser(event.objectClicked.id, event.objectClicked.firstName + ' ' + event.objectClicked.lastName);
      }
    }
    if (event.buttonActionId === 'edit') {
      const navigationExtras: NavigationExtras = {
        state: {
          userId: event.objectClicked.id // Database id
        }
      };
      let fullName = '';
      if (event.objectClicked.userType === 'COMPANY') {
        fullName = `${event.objectClicked.socialReason}`;
      } else {
        fullName = `${event.objectClicked.firstName} ${event.objectClicked.lastName}`;
      }
      this.router.navigate(['/pages/user-management/internal-user-management/update-user', fullName], navigationExtras);
    }
    if (event.buttonActionId === 'delete') {
      this.deleteUserModal.show();
      this.userId = event.objectClicked.id;
      this.userName = event.objectClicked.firstName + ' ' + event.objectClicked.lastName;
    }
  }

  handleOnViewUser(id: string, label: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        userId: id // Database id
      }
    };
    this.router.navigate(['view-user', label], navigationExtras);
  }

  protected handleOnDeleteUser(): void {

    this.userService.deleteInternalUser(this.userId).subscribe({
      next: (data) => {
        this.toastrService.onSuccess(this.translatePipe.transform('users.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.updateTable();
        this.deleteUserModal.hide();
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform('users.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
    this.actionButton.push(new ButtonActionModel('delete', 'button.DELETE', 'btn btn-icon', 'feather icon-trash'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('users.USERNAME', 'representativeName', false));
    // this.tableColumns.push(new CustomTableColonneModel('users.USERNAME', 'username', false));
    this.tableColumns.push(new CustomTableColonneModel('users.EMAIL', 'email', false));
    this.tableColumns.push(new CustomTableColonneModel('users.STATUS', 'statut', false));
  }
  public redirectToAddUser(): void {
    this.router.navigate(['/pages/user-management/internal-user-management/add']);
  }
  private updateTable(): void {
    this.users = [];
    this.loadUsers();
    this.cdRef.detectChanges();
  }
}
