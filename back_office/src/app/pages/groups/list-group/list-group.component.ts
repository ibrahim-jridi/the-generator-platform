import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { TranslatePipe } from '@ngx-translate/core';
import { PaginationArgs, PaginationSortArgs, PaginationSortOrderType } from '../../../shared/models/paginationArgs.model';
import { AppToastNotificationService } from 'src/app/shared/services/appToastNotification.service';
import { GroupsService } from 'src/app/shared/services/groups.service';
import { UiModalComponent } from 'src/app/theme/shared/components/modal/ui-modal/ui-modal.component';
import { ButtonActionModel } from '../../../theme/shared/components/custom-table/model/button-action.model';
import { CustomTableColonneModel } from '../../../theme/shared/components/custom-table/model/custom-table-colonne.model';
import { FilterValuesModel } from '../../../shared/models/filter-values-model';
import { DateformatPipe } from '../../../shared/pipes/date-transform.pipe';

@Component({
  selector: 'app-list-group',
  templateUrl: './list-group.component.html',
  styleUrl: './list-group.component.scss',
  providers: [DateformatPipe]
})
export class ListGroupComponent implements OnInit {
  public paginationSortArgs: PaginationSortArgs = new PaginationSortArgs('lastModifiedDate', PaginationSortOrderType.DESC);
  protected tableColumns: CustomTableColonneModel[] = new Array<CustomTableColonneModel>();
  protected actionButton: ButtonActionModel[] = new Array<ButtonActionModel>();
  public paginationArgs: PaginationArgs;
  public totalCount: number;
  public pageIndex: number = 0;
  protected pageSize: number;
  protected groups: any[] = [];
  protected groupId: string;
  protected groupSelected: any = {};
  protected userId: string;
  protected viewGroupsOfUser: boolean = false;
  protected groupName: string;
  public state = window.history.state;

  protected filterValues: FilterValuesModel[];
  @ViewChild('deleteGroupModal') deleteGroupModal: UiModalComponent;

  constructor(
    private groupsService: GroupsService,
    private router: Router,
    private route: ActivatedRoute,
    private translatePipe: TranslatePipe,
    private toastrService: AppToastNotificationService,
    private cdRef: ChangeDetectorRef,
    private dateTransformPipe: DateformatPipe
  ) {}

  public ngOnInit(): void {
    this.userId = this.state.userId;
    if (this.state.viewGroupsOfUser != null) {
      this.viewGroupsOfUser = this.state.viewGroupsOfUser;
    }
    this.pageSize = 10;
    this.loadGroups();
    this.initTableAction();
    this.initTableColumns();
  }

  private loadGroups(): void {
    this.paginationArgs = {
      sort: this.paginationSortArgs.sort,
      page: this.pageIndex,
      size: this.pageSize
    };

    this.groupsService.getAllGroups(this.filterValues, this.paginationArgs).subscribe({
      next: (response) => {
        const totalCountHeader = response?.headers?.get('X-Total-Count');
        this.totalCount = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
        this.groups = response.body?.map((item, index) => ({
          ...item,
          createdDate: this.dateTransformPipe.transform(item.createdDate),
          index: this.pageSize * this.pageIndex + index + 1,
          actions: [
            { action: 'button.UNLOCK', show: !item.isActive },
            { action: 'button.LOCK', show: item.isActive }
          ]
        }));
      },
      error: () => {},
      complete: () => {
        this.formatGroupList();
      }
    });

    this.cdRef.detectChanges();
  }

  private formatGroupList(): void {
    this.groups.forEach((group) => {
      group.status = group.isActive ? this.translatePipe.transform('groups.ACTIVE') : this.translatePipe.transform('groups.INACTIVE');
    });
  }

  private initTableAction(): void {
    this.actionButton.push(new ButtonActionModel('lock', 'button.LOCK', 'btn btn-icon', 'feather icon-lock'));
    this.actionButton.push(new ButtonActionModel('unlock', 'button.UNLOCK', 'btn btn-icon', 'feather icon-unlock'));
    this.actionButton.push(new ButtonActionModel('view', 'tooltip.VIEW', 'btn btn-icon ', 'feather icon-eye'));
    this.actionButton.push(new ButtonActionModel('delete', 'tooltip.DELETE', 'btn btn-icon', 'feather icon-trash'));
  }

  private initTableColumns(): void {
    this.tableColumns.push(new CustomTableColonneModel('groups.NAME', 'label', false));
    this.tableColumns.push(new CustomTableColonneModel('groups.CREATED_DATE', 'createdDate', false));
    this.tableColumns.push(new CustomTableColonneModel('groups.USERS', 'numberOfUsers', false));
    this.tableColumns.push(new CustomTableColonneModel('groups.STATUS', 'status', false));
  }

  public nextPage(event: any): void {
    this.filterValues = event.filterValues;
    this.pageIndex = event.pageNumber - 1;
    this.loadGroups();
  }

  public onPageSizeChange(newPageSize: number) {
    this.pageIndex = 0;
    this.pageSize = newPageSize;
    this.loadGroups();
  }

  protected buttonAction(event: any): void {
    this.groupSelected = this.groups.find((group) => group.id === event.objectClicked.id);

    if (event.buttonActionId === 'view') {
      this.handleOnViewGroup(event.objectClicked.id, event.objectClicked.label);
    }

    if (event.buttonActionId === 'delete') {
      this.deleteGroupModal.show();
      this.groupId = event.objectClicked.id;
      this.groupName = event.objectClicked.label;
    }
    if (event.buttonActionId === 'lock' || event.buttonActionId === 'unlock') {
      this.activateOrDeactivateGroup(event.objectClicked.id, event.buttonActionId);
    }
  }

  protected handleOnViewGroup(id: number, label: string): void {
    const navigationExtras: NavigationExtras = {
      relativeTo: this.route,
      state: {
        groupId: id // Database Id
      }
    };

    this.router.navigate(['view-group', label], navigationExtras);
  }

  protected activateOrDeactivateGroup(id, event: string) {
    this.groupsService.activateOrDeactivateGroup(id).subscribe({
      next: () => {
        if (event === 'lock') {
          this.toastrService.onSuccess(this.translatePipe.transform('groups.DEACTIVATED_SUCCESSFULLY'), this.translatePipe.transform('menu.SUCCESS'));
        } else {
          this.toastrService.onSuccess(this.translatePipe.transform('groups.ACTIVATED_SUCCESSFULLY'), this.translatePipe.transform('menu.SUCCESS'));
        }
      },
      error: (err) => {
        this.toastrService.onError(
          this.translatePipe.transform('groups.FAILED_TO_ACTIVATED_OR_DEACTIVATED_GROUP'),
          this.translatePipe.transform('menu.ERROR')
        );
      }
    });
    this.updateTable();
  }

  protected safeDeleteGroup(): void {
    this.groupsService.deleteGroup(this.groupId).subscribe({
      next: () => {
        this.toastrService.onSuccess(this.translatePipe.transform('groups.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.updateTable();
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('groups.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  protected permanentlyDeleteGroup(): void {
    this.groupsService.permanentlyDeleteGroup(this.groupId).subscribe({
      next: () => {
        this.toastrService.onSuccess(this.translatePipe.transform('groups.DELETE_SUCCESS'), this.translatePipe.transform('menu.SUCCESS'));
        this.updateTable();
      },
      error: () => {
        this.toastrService.onError(this.translatePipe.transform('groups.DELETE_FAILED'), this.translatePipe.transform('menu.ERROR'));
      }
    });
  }

  private updateTable(): void {
    this.groups = [];
    this.loadGroups();
    this.cdRef.detectChanges();
  }

  protected reassignUsers(groupId: string, label: string): void {
    const navigationExtras: NavigationExtras = {
      state: {
        groupId: groupId,
        groupName: label
      }
    };
    this.router.navigate(['/pages/group-management/list-users-group/' + groupId], navigationExtras);
  }

  protected redirectToAddGroup(): void {
    this.router.navigate(['/pages/group-management/add']);
  }
}
