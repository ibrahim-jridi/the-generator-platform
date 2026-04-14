import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../../shared/models/paginationArgs.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { ButtonActionModel } from '../../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { UserService } from '../../../../shared/services/user.service';

@Component({
  selector: 'app-external-user-list',
  templateUrl: './external-user-list.component.html',
  styleUrl: './external-user-list.component.scss'
})
export class ExternalUserListComponent implements OnInit {
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  protected user_external: any[] = [];
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  protected userId: string;
  private paginationArgs: PaginationArgs;
  protected totalElements: number;
  @ViewChild('deleteUserModal') protected deleteUserModal: UiModalComponent;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private toastrService: AppToastNotificationService,
    private userService: UserService
  ) {}

  public ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.loadUserExternal();
    this.initTableColumns();
    this.initTableAction();
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.loadUserExternal();
  }

  public redirectToAddExternalUser(): void {
    this.router.navigate(['/pages/user-management/external-user-management/add']);
  }

  handleOnUpdateUserExternal(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        userId: 1 // Database id
      }
    };
    this.router.navigate(['update-user-external', label], navigationExtras);
  }

  handleOnViewUserExternal(id: string, label: string) {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        userId: 1, // Database id
        userName: label // Database id
      }
    };
    this.router.navigate(['view-user-external', label], navigationExtras);
  }

  protected handleOnDeleteUserExternal(): void {
    this.userService.deleteUserById(this.userId).subscribe({
      next: (data) => {
        this.toastrService.onSuccess(this.translatePipe.transform('users.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));

        this.updateTable();
      },
      error: (error) => {
        this.toastrService.onError(this.translatePipe.transform('users.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewUserExternal(event.objectClicked.id, event.objectClicked.firstName + ' ' + event.objectClicked.lastName);
      ///this.router.navigate(['pages/user-management/external-user-management/view'])
    }
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateUserExternal(event.objectClicked.id, event.objectClicked.firstName + ' ' + event.objectClicked.lastName);
    }
    if (event.buttonActionId === 'delete') {
      this.deleteUserModal.show();
      this.userId = event.objectClicked.id;
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
    this.actionButton.push(new ButtonActionModel('delete', 'button.DELETE', 'btn btn-icon', 'feather icon-trash'));
  }

  private loadUserExternal(): void {
    this.paginationArgs = { sort: this.paginationSortArgs.sort, page: this.page, size: this.pageSize };
    this.userService.getUserExternals().subscribe((data) => {
      this.user_external = data;
    });
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('users.ID', 'id', false));
    this.tableColumns.push(new CustomTableColonneModel('users.FIRST_NAME', 'firstName', false));
    this.tableColumns.push(new CustomTableColonneModel('users.LAST_NAME', 'lastName', false));
    this.tableColumns.push(new CustomTableColonneModel('users.EMAIL', 'email', false));
    this.tableColumns.push(new CustomTableColonneModel('users.CIN', 'cin', false));
    this.tableColumns.push(new CustomTableColonneModel('users.STATUS', 'status', false));
  }

  private updateTable(): void {
    this.user_external = [];
    this.loadUserExternal();
  }
}
