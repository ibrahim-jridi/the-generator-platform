import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { RoleService } from '../../../shared/services/role.service';
import { RoleCriteria } from '../../../shared/models/roleCriteria.model';
import { DateformatPipe } from '../../../shared/pipes/date-transform.pipe';
import { AppToastNotificationService } from '../../../shared/services/appToastNotification.service';

@Component({
  selector: 'app-role-list',
  templateUrl: './role-list.component.html',
  styleUrl: './role-list.component.scss',
  providers: [DateformatPipe]
})
export class RoleListComponent implements OnInit {
  private paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('last_modified_date', PaginationSortOrderType.DESC);
  protected roles: any[] = [];
  protected pageSize: number;
  protected page: number;
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  @ViewChild('deleteRoleModal') protected deleteRoleModal: UiModalComponent;
  protected roleId: string;
  protected roleName: string;
  protected totalElements: number;
  protected hasUsers: boolean = false;
  private paginationArgs: PaginationArgs;
  private criteria: RoleCriteria = new RoleCriteria();
  protected viewGroupsOfUser: boolean = false;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private toastrService: AppToastNotificationService,
    private roleService: RoleService,
    private dateTransformPipe: DateformatPipe,
    private cdRef: ChangeDetectorRef
  ) {}

  public ngOnInit(): void {
    this.page = 0;
    this.pageSize = 5;
    this.loadRole();
    this.initTableAction();
    this.initTableColumns();
  }

  public nextPage(event): void {
    this.page = event.pageNumber;
    this.loadRole();
  }

  private loadRole(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.page,
      size: this.pageSize
    };

    this.roleService.getAllRoles(this.criteria, this.paginationArgs).subscribe((data) => {
      this.roles = data.map((item, index) => ({
        ...item,
        createdDate: this.dateTransformPipe.transformDateAndHours(item.createdDate),
        permissionCount: item.authorities.length,
        status: item.isActive ? this.translatePipe.transform('groups.ACTIVE') : this.translatePipe.transform('groups.INACTIVE'),
        index: this.page * this.pageSize + index + 1,
        actions: [
          { action: 'button.UNLOCK', show: !item.isActive },
          { action: 'button.LOCK', show: item.isActive }
        ]
      }));
    });
  }

  public redirectToAddRole(): void {
    this.router.navigate(['/pages/role-management/add']);
  }

  private handleOnViewRole(id: string, label: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        roleId: id, // Database id
        userName: label // Database id
      }
    };
    this.router.navigate(['view-role', label], navigationExtras);
  }

  private handleOnUpdateRole(id: string, label: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        roleId: id // Database id
      }
    };
    this.router.navigate(['update-role', label], navigationExtras);
  }

  private updateTable(): void {
    this.roles = [];
    this.loadRole();
    this.cdRef.detectChanges();
  }

  public buttonAction(event: any): void {
    if (event.buttonActionId === 'view') {
      this.handleOnViewRole(event.objectClicked.id, event.objectClicked.label);
    }
    if (event.buttonActionId === 'edit') {
      this.handleOnUpdateRole(event.objectClicked.id, event.objectClicked.label);
    }
    if (event.buttonActionId === 'delete') {
      this.deleteRoleModal.show();
      this.roleId = event.objectClicked.id;
      this.roleName = event.objectClicked.label;
      this.hasUsers = this.roles.find((role) => role.id === this.roleId).hasUsers;
    }
    if (event.buttonActionId === 'lock' || event.buttonActionId === 'unlock') {
      this.activateOrDeactivateRole(event.objectClicked.id, event.buttonActionId);
    }
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('lock', 'button.LOCK', 'btn btn-icon', 'feather icon-lock'));
    this.actionButton.push(new ButtonActionModel('unlock', 'button.UNLOCK', 'btn btn-icon', 'feather icon-unlock'));
    this.actionButton.push(new ButtonActionModel('view', 'button.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('edit', 'button.EDIT', 'btn btn-icon fa-light fa-pen-to-square', 'feather icon-edit'));
    this.actionButton.push(new ButtonActionModel('delete', 'button.DELETE', 'btn btn-icon', 'feather icon-trash'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('role.LABEL', 'label', false));
    this.tableColumns.push(new CustomTableColonneModel('role.CREATION_DATE', 'createdDate', false));
    this.tableColumns.push(new CustomTableColonneModel('role.NUMBER_PERMISSION', 'permissionCount', false));
    this.tableColumns.push(new CustomTableColonneModel('role.STATUS', 'status', false));
  }

  private activateOrDeactivateRole(id, event: string): void {
    this.roleService.activateOrDeactivateRoleById(id).subscribe({
      next: () => {
        this.updateTable();
        if (event === 'lock') {
          this.toastrService.onSuccess(this.translatePipe.transform('role.DEACTIVATED_SUCCESSFULLY'), this.translatePipe.transform('menu.SUCCESS'));
        } else {
          this.toastrService.onSuccess(this.translatePipe.transform('role.ACTIVATED_SUCCESSFULLY'), this.translatePipe.transform('menu.SUCCESS'));
        }
      },
      error: () => {
        this.toastrService.onError(
          this.translatePipe.transform('role.FAILED_TO_ACTIVATED_OR_DEACTIVATED_ROLE'),
          this.translatePipe.transform('menu.ERROR')
        );
      }
    });
  }

  protected safeDeleteRole(): void {
    this.roleService.deleteRoleById(this.roleId).subscribe({
      next: (res) => {
        this.updateTable();
        this.deleteRoleModal.hide();
        this.toastrService.onSuccess(this.translatePipe.transform('role.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('role.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected permanentlyDeleteRole(): void {
    this.roleService.permanentlyDeleteRole(this.roleId).subscribe({
      next: () => {
        this.toastrService.onSuccess(this.translatePipe.transform('role.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.updateTable();
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('role.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
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
}
